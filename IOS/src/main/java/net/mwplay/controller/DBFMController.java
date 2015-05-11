package net.mwplay.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.mwplay.beans.Channel;
import net.mwplay.beans.Song;
import net.mwplay.utils.HttpController;
import net.mwplay.utils.OnChannelChangListener;
import net.mwplay.utils.PrintTool;
import net.mwplay.view.CircleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.robovm.apple.coregraphics.CGPoint;
import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSArray.AsStringListMarshaler;
import org.robovm.apple.foundation.NSData;
import org.robovm.apple.foundation.NSDictionary;
import org.robovm.apple.foundation.NSIndexPath;
import org.robovm.apple.foundation.NSObject;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.foundation.NSTimer;
import org.robovm.apple.foundation.NSURL;
import org.robovm.apple.mediaplayer.MPMoviePlayerController;
import org.robovm.apple.uikit.UIBlurEffect;
import org.robovm.apple.uikit.UIBlurEffectStyle;
import org.robovm.apple.uikit.UIButton;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UIControlState;
import org.robovm.apple.uikit.UIImage;
import org.robovm.apple.uikit.UIImageView;
import org.robovm.apple.uikit.UILabel;
import org.robovm.apple.uikit.UIScrollView;
import org.robovm.apple.uikit.UIStoryboardSegue;
import org.robovm.apple.uikit.UITableView;
import org.robovm.apple.uikit.UITableViewCell;
import org.robovm.apple.uikit.UITableViewCellEditingStyle;
import org.robovm.apple.uikit.UITableViewDataSource;
import org.robovm.apple.uikit.UITableViewDelegate;
import org.robovm.apple.uikit.UITableViewRowAction;
import org.robovm.apple.uikit.UITableViewScrollPosition;
import org.robovm.apple.uikit.UIView;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.apple.uikit.UIVisualEffectView;
import org.robovm.objc.Selector;
import org.robovm.objc.annotation.BindSelector;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.objc.annotation.IBAction;
import org.robovm.objc.annotation.IBOutlet;
import org.robovm.objc.annotation.Method;
import org.robovm.objc.block.Block1;
import org.robovm.objc.block.VoidBlock1;
import org.robovm.rt.bro.annotation.Bridge;
import org.robovm.rt.bro.annotation.ByVal;
import org.robovm.rt.bro.annotation.Callback;
import org.robovm.rt.bro.annotation.MachineSizedFloat;
import org.robovm.rt.bro.annotation.MachineSizedSInt;

import com.android.okhttp.Failure;
import com.android.okhttp.Response;
import com.android.okhttp.Response.Receiver;

@CustomClass("DBFMController")
public class DBFMController extends UIViewController implements
		UITableViewDataSource, OnChannelChangListener, UITableViewDelegate {
	CircleView mCircleView;// 显示碟片状的圆形图片
	CircleView midCircleView;// label的背景
	UIImageView mBg;// 背景图片用于添加模糊效果
	UITableView mSongList;// 歌曲列表view
	UILabel mTimeLabel;// 歌曲播放时间
	UIImageView mProgressBar;

	UIButton mReFresh, mLast, mPlay, mNext;

	List<Channel> channels;// 频道列表
	List<Song> songs;// 歌曲列表

	public final static String CHANNELS_URL = "http://www.douban.com/j/app/radio/channels";
	public final static String SONGS_URL = "http://douban.fm/j/mine/playlist?channel=0&type=n&from=mainsite";

	Map<String, UIImage> cache;

	MPMoviePlayerController mPlayerController;// 播放器
	NSTimer mTimer;// 定时器

	// Timer mTimer;

	@IBOutlet
	public void setUIImageView(CircleView view) {
		this.mCircleView = view;

	}

	@IBOutlet
	public void setTimeBg(CircleView view) {
		this.midCircleView = view;
	}

	@IBOutlet
	public void setBg(UIImageView view) {
		this.mBg = view;
	}

	@IBOutlet
	public void setSongList(UITableView view) {
		this.mSongList = view;
	}

	@IBOutlet
	public void setLabel(UILabel view) {
		this.mTimeLabel = view;
	}

	@IBOutlet
	public void setProgressBar(UIImageView mProgressBar) {
		this.mProgressBar = mProgressBar;
	}

	@IBOutlet
	public void setReFresh(UIButton mReFresh) {
		this.mReFresh = mReFresh;
	}

	@IBOutlet
	public void setLast(UIButton mLast) {
		this.mLast = mLast;
	}

	@IBOutlet
	public void setPlay(UIButton mPlay) {
		this.mPlay = mPlay;
	}

	@IBOutlet
	public void setNext(UIButton mNext) {
		this.mNext = mNext;
	}

	boolean isPlay = true;

	@IBAction
	public void onClickPlay() {
		isPlay = !isPlay;

		if (isPlay) {
			mPlayerController.play();
			mPlay.setImage(UIImage.create("pause"), UIControlState.Normal);
		} else {
			mPlayerController.pause();
			mPlay.setImage(UIImage.create("play"), UIControlState.Normal);
		}
	}
	@IBAction
	public void onReFreshClick() {
		mPlayerController.stop();
		mPlayerController.play();
	}
	@IBAction
	public void onLastClick() {
		int row = (int) mSongList.getIndexPathForSelectedRow().getRow();
		
		if (row > 0) {
			select(row - 1);
		}
	}
	@IBAction
	public void onNextClick() {
		int row = (int) mSongList.getIndexPathForSelectedRow().getRow();
		
		if (row + 1 < songs.size()) {
			select(row + 1);
		}
	}

	@Override
	@Method(selector = "viewDidLoad")
	public void viewDidLoad() {
		super.viewDidLoad();
		this.mCircleView.useCircle();
		this.midCircleView.useCircle();

		channels = new ArrayList<Channel>();
		songs = new ArrayList<Song>();
		cache = new HashMap<String, UIImage>();
		mPlayerController = new MPMoviePlayerController();
		// mTimer = new Timer();

		// 背景透明
		mSongList.setBackgroundColor(UIColor.clear());

		addBlurEffect();
		init();

		mSongList.setDataSource(this);
		mSongList.setDelegate(this);
	}

	private void init() {
		// 获取频道列表
		HttpController.getInstance().onSearch(CHANNELS_URL, new Receiver() {

			@Override
			public void onResponse(Response response) throws IOException {
				onReceivedChannels(response.body().string());
			}

			@Override
			public void onFailure(Failure failure) {
				PrintTool.println(failure.exception().getMessage());
			}
		});

		getSongById("0");
	}

	public void getSongById(String id) {
		// 获取频道0的歌曲
		HttpController.getInstance().onSearch(SONGS_URL.replace("0", id),
				new Receiver() {
					@Override
					public void onResponse(Response response)
							throws IOException {
						getData(response.body().string());
					}

					@Override
					public void onFailure(Failure failure) {
						PrintTool.println(failure.exception().getMessage());
					}
				});
	}

	public void onReceivedChannels(String msg) {
		channels.clear();

		try {
			JSONObject mJsonObject = new JSONObject(msg);
			JSONArray channelsArray = mJsonObject.getJSONArray("channels");

			for (int i = 0; i < channelsArray.length(); i++) {
				JSONObject obj = channelsArray.getJSONObject(i);
				Channel channel = new Channel();
				channel.setId(obj.getInt("channel_id"));
				channel.setName(obj.getString("name"));
				channel.setName_en(obj.getString("name_en"));
				channel.setAbbr_en(obj.getString("abbr_en"));
				channel.setSeq_id(obj.getInt("seq_id"));

				channels.add(channel);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	// Get songs
	private void getData(String json) {
		songs.clear();

		try {
			JSONObject mJsonObject = new JSONObject(json);
			JSONArray channelsArray = mJsonObject.getJSONArray("song");

			for (int i = 0; i < channelsArray.length(); i++) {
				JSONObject obj = channelsArray.getJSONObject(i);
				Song song = new Song();
				song.setAid(obj.getString("aid"));
				song.setArtist(obj.getString("artist"));
				song.setPicture(obj.getString("picture"));
				song.setTitle(obj.getString("title"));
				song.setUrl(obj.getString("url"));

				songs.add(song);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		mSongList.reloadData();
	}

	private void addBlurEffect() {
		// 设置背景模糊效果
		UIBlurEffect mBlurEffect = UIBlurEffect.create(UIBlurEffectStyle.Light);
		UIVisualEffectView mEffectView = new UIVisualEffectView(mBlurEffect);
		mEffectView.setFrame(getView().getFrame());
		mBg.addSubview(mEffectView);
	}

	// 选择了哪一行
	private void select(int row) {
		NSIndexPath mIndexPath = NSIndexPath.createWithRow(row, 0);

		mSongList.selectRow(mIndexPath, false, UITableViewScrollPosition.Top);

		Song song = songs.get(row);
		setBg(song.getPicture());

		setAudio(song.getUrl());

		if (!isPlay) {
			onClickPlay();
		}

		this.mCircleView.rotation();
	}

	// 获取图片缓存
	public void onGetCache(final String url, final UIImageView view) {
		if (cache.get(url) == null) {
			HttpController.getInstance().onSearch(url.toString(),
					new Receiver() {

						@Override
						public void onResponse(Response arg0)
								throws IOException {
							NSData mData = new NSData(arg0.body().bytes());
							UIImage mImage = UIImage.create(mData);
							view.setImage(mImage);

							cache.put(url, mImage);
						}

						@Override
						public void onFailure(Failure arg0) {

						}
					});

		} else {
			view.setImage(cache.get(url));
		}
	}

	// 播放音乐
	private void setAudio(String url) {
		mPlayerController.stop();
		mPlayerController.setContentURL(new NSURL(url));
		mPlayerController.play();

		mTimeLabel.setText("00:00");

		// mTimer.schedule(new TimerTask() {
		// @Override
		// public void run() {
		// onUpdate();
		// }
		// }, 1000, 1000);

		mTimer = NSTimer.create(1, new VoidBlock1<NSTimer>() {

			@Override
			public void invoke(NSTimer a) {
				onUpdate();
			}
		}, true);

	}

	public void onUpdate() {
		double time = mPlayerController.getCurrentPlaybackTime();
		if (time > 0.0) {
			double totalTime = mPlayerController.getDuration();
			float per = (float) (time / totalTime);
			// 进度条的宽度
			mProgressBar.getFrame().getSize()
					.setWidth(getView().getFrame().getWidth() * per);

			int sec = (int) (time % 60);
			int minute = (int) (time / 60);

			String timeS = "";
			if (minute < 10) {
				timeS = "0" + minute + ":";
			} else {
				timeS = "" + minute + ":";
			}

			if (sec < 10) {
				timeS += "0" + sec;
			} else {
				timeS += "" + sec;
			}

			PrintTool.println(timeS);
			mTimeLabel.setText(timeS);
		}
	}

	// 设置背景
	private void setBg(String url) {
		onGetCache((url), mBg);
		onGetCache((url), mCircleView);
	}

	@Override
	@Method(selector = "tableView:numberOfRowsInSection:")
	@MachineSizedSInt
	public long getNumberOfRowsInSection(UITableView tableView,
			@MachineSizedSInt long section) {
		return songs.size();
	}

	@Override
	@Method(selector = "tableView:cellForRowAtIndexPath:")
	public UITableViewCell getCellForRow(UITableView tableView,
			NSIndexPath indexPath) {
		final UITableViewCell mCell = tableView.dequeueReusableCell("douban",
				indexPath);
		mCell.setBackgroundColor(UIColor.clear());

		final Song song = songs.get((int) indexPath.getRow());

		if (song != null) {
			mCell.getTextLabel().setText(song.getTitle());
			mCell.getDetailTextLabel().setText(song.getArtist());
			mCell.getImageView().setImage(UIImage.create("thumb"));

			onGetCache((song.getPicture()), mCell.getImageView());
		}

		return mCell;
	}

	@Override
	@Method(selector = "numberOfSectionsInTableView:")
	@MachineSizedSInt
	public long getNumberOfSections(UITableView tableView) {
		return 1;
	}

	@Override
	@Method(selector = "tableView:titleForHeaderInSection:")
	public String getTitleForHeader(UITableView tableView,
			@MachineSizedSInt long section) {
		return null;
	}

	@Override
	@Method(selector = "tableView:titleForFooterInSection:")
	public String getTitleForFooter(UITableView tableView,
			@MachineSizedSInt long section) {
		return null;
	}

	@Override
	@Method(selector = "tableView:canEditRowAtIndexPath:")
	public boolean canEditRow(UITableView tableView, NSIndexPath indexPath) {
		return false;
	}

	@Override
	@Method(selector = "tableView:canMoveRowAtIndexPath:")
	public boolean canMoveRow(UITableView tableView, NSIndexPath indexPath) {
		return false;
	}

	@Override
	@Method(selector = "sectionIndexTitlesForTableView:")
	@org.robovm.rt.bro.annotation.Marshaler(AsStringListMarshaler.class)
	public List<String> getSectionIndexTitles(UITableView tableView) {
		return null;
	}

	@Override
	@Method(selector = "tableView:sectionForSectionIndexTitle:atIndex:")
	@MachineSizedSInt
	public long getSectionForSectionIndexTitle(UITableView tableView,
			String title, @MachineSizedSInt long index) {
		return 0;
	}

	@Override
	@Method(selector = "tableView:commitEditingStyle:forRowAtIndexPath:")
	public void commitEditingStyleForRow(UITableView tableView,
			UITableViewCellEditingStyle editingStyle, NSIndexPath indexPath) {

	}

	@Override
	@Method(selector = "tableView:moveRowAtIndexPath:toIndexPath:")
	public void moveRow(UITableView tableView, NSIndexPath sourceIndexPath,
			NSIndexPath destinationIndexPath) {

	}

	// 传送数据到频道列表
	@Override
	@Method(selector = "prepareForSegue:sender:")
	public void prepareForSegue(UIStoryboardSegue segue, NSObject sender) {
		super.prepareForSegue(segue, sender);
		if (segue.getIdentifier().equals("tochannel") && sender != null) {
			ChannelViewController channelViewController = (ChannelViewController) segue
					.getDestinationViewController();
			channelViewController.setChannels(channels);
			channelViewController.setChangListener(this);
		}
	}

	@Override
	public void onChannelChanged(String id) {
		// 网络
		getSongById(id);
	}

	@Override
	@Method(selector = "scrollViewDidScroll:")
	public void didScroll(UIScrollView scrollView) {
		// TODO Auto-generated method stub

	}

	@Override
	@Method(selector = "scrollViewDidZoom:")
	public void didZoom(UIScrollView scrollView) {
		// TODO Auto-generated method stub

	}

	@Override
	@Method(selector = "scrollViewWillBeginDragging:")
	public void willBeginDragging(UIScrollView scrollView) {

	}

	@Override
	@Method(selector = "scrollViewWillEndDragging:withVelocity:targetContentOffset:")
	public void willEndDragging(UIScrollView scrollView,
			@ByVal CGPoint velocity, CGPoint targetContentOffset) {
		// TODO Auto-generated method stub

	}

	@Override
	@Method(selector = "scrollViewDidEndDragging:willDecelerate:")
	public void didEndDragging(UIScrollView scrollView, boolean decelerate) {
		// TODO Auto-generated method stub

	}

	@Override
	@Method(selector = "scrollViewWillBeginDecelerating:")
	public void willBeginDecelerating(UIScrollView scrollView) {
		// TODO Auto-generated method stub

	}

	@Override
	@Method(selector = "scrollViewDidEndDecelerating:")
	public void didEndDecelerating(UIScrollView scrollView) {
		// TODO Auto-generated method stub

	}

	@Override
	@Method(selector = "scrollViewDidEndScrollingAnimation:")
	public void didEndScrollingAnimation(UIScrollView scrollView) {
		// TODO Auto-generated method stub

	}

	@Override
	@Method(selector = "viewForZoomingInScrollView:")
	public UIView getViewForZooming(UIScrollView scrollView) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Method(selector = "scrollViewWillBeginZooming:withView:")
	public void willBeginZooming(UIScrollView scrollView, UIView view) {
		// TODO Auto-generated method stub

	}

	@Override
	@Method(selector = "scrollViewDidEndZooming:withView:atScale:")
	public void didEndZooming(UIScrollView scrollView, UIView view,
			@MachineSizedFloat double scale) {
		// TODO Auto-generated method stub

	}

	@Override
	@Method(selector = "scrollViewShouldScrollToTop:")
	public boolean shouldScrollToTop(UIScrollView scrollView) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Method(selector = "scrollViewDidScrollToTop:")
	public void didScrollToTop(UIScrollView scrollView) {
		// TODO Auto-generated method stub

	}

	@Override
	@Method(selector = "tableView:willDisplayCell:forRowAtIndexPath:")
	public void willDisplayCell(UITableView tableView, UITableViewCell cell,
			NSIndexPath indexPath) {
		// TODO Auto-generated method stub

	}

	@Override
	@Method(selector = "tableView:willDisplayHeaderView:forSection:")
	public void willDisplayHeaderView(UITableView tableView, UIView view,
			@MachineSizedSInt long section) {
	}

	@Override
	@Method(selector = "tableView:willDisplayFooterView:forSection:")
	public void willDisplayFooterView(UITableView tableView, UIView view,
			@MachineSizedSInt long section) {
	}

	@Override
	@Method(selector = "tableView:didEndDisplayingCell:forRowAtIndexPath:")
	public void didEndDisplayingCell(UITableView tableView,
			UITableViewCell cell, NSIndexPath indexPath) {
	}

	@Override
	@Method(selector = "tableView:didEndDisplayingHeaderView:forSection:")
	public void didEndDisplayingHeaderView(UITableView tableView, UIView view,
			@MachineSizedSInt long section) {
	}

	@Override
	@Method(selector = "tableView:didEndDisplayingFooterView:forSection:")
	public void didEndDisplayingFooterView(UITableView tableView, UIView view,
			@MachineSizedSInt long section) {
	}

	@Override
	@Method(selector = "tableView:heightForRowAtIndexPath:")
	@MachineSizedFloat
	public double getHeightForRow(UITableView tableView, NSIndexPath indexPath) {
		return 40;
	}

	@Override
	@Method(selector = "tableView:heightForHeaderInSection:")
	@MachineSizedFloat
	public double getHeightForHeader(UITableView tableView,
			@MachineSizedSInt long section) {
		return 0;
	}

	@Override
	@Method(selector = "tableView:heightForFooterInSection:")
	@MachineSizedFloat
	public double getHeightForFooter(UITableView tableView,
			@MachineSizedSInt long section) {
		return 0;
	}

	@Override
	@Method(selector = "tableView:estimatedHeightForRowAtIndexPath:")
	@MachineSizedFloat
	public double getEstimatedHeightForRow(UITableView tableView,
			NSIndexPath indexPath) {
		return 0;
	}

	@Override
	@Method(selector = "tableView:estimatedHeightForHeaderInSection:")
	@MachineSizedFloat
	public double getEstimatedHeightForHeader(UITableView tableView,
			@MachineSizedSInt long section) {
		return 0;
	}

	@Override
	@Method(selector = "tableView:estimatedHeightForFooterInSection:")
	@MachineSizedFloat
	public double getEstimatedHeightForFooter(UITableView tableView,
			@MachineSizedSInt long section) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@Method(selector = "tableView:viewForHeaderInSection:")
	public UIView getViewForHeader(UITableView tableView,
			@MachineSizedSInt long section) {
		return null;
	}

	@Override
	@Method(selector = "tableView:viewForFooterInSection:")
	public UIView getViewForFooter(UITableView tableView,
			@MachineSizedSInt long section) {
		return null;
	}

	@Override
	@Method(selector = "tableView:accessoryButtonTappedForRowWithIndexPath:")
	public void accessoryButtonTapped(UITableView tableView,
			NSIndexPath indexPath) {
	}

	@Override
	@Method(selector = "tableView:shouldHighlightRowAtIndexPath:")
	public boolean shouldHighlightRow(UITableView tableView,
			NSIndexPath indexPath) {
		return true;
	}

	@Override
	@Method(selector = "tableView:didHighlightRowAtIndexPath:")
	public void didHighlightRow(UITableView tableView, NSIndexPath indexPath) {
	}

	@Override
	@Method(selector = "tableView:didUnhighlightRowAtIndexPath:")
	public void didUnhighlightRow(UITableView tableView, NSIndexPath indexPath) {
	}

	@Override
	@Method(selector = "tableView:willSelectRowAtIndexPath:")
	public NSIndexPath willSelectRow(UITableView tableView,
			NSIndexPath indexPath) {
		// TODO Auto-generated method stub
		return indexPath;
	}

	@Override
	@Method(selector = "tableView:willDeselectRowAtIndexPath:")
	public NSIndexPath willDeselectRow(UITableView tableView,
			NSIndexPath indexPath) {
		// TODO Auto-generated method stub
		return indexPath;
	}

	@Override
	@Method(selector = "tableView:didSelectRowAtIndexPath:")
	public void didSelectRow(UITableView tableView, NSIndexPath indexPath) {
		int row = (int) indexPath.getRow();
		// clicked
		select(row);
	}

	@Override
	@Method(selector = "tableView:didDeselectRowAtIndexPath:")
	public void didDeselectRow(UITableView tableView, NSIndexPath indexPath) {
		// TODO Auto-generated method stub

	}

	@Override
	@Method(selector = "tableView:editingStyleForRowAtIndexPath:")
	public UITableViewCellEditingStyle getEditingStyleForRow(
			UITableView tableView, NSIndexPath indexPath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Method(selector = "tableView:titleForDeleteConfirmationButtonForRowAtIndexPath:")
	public String getTitleForDeleteConfirmationButton(UITableView tableView,
			NSIndexPath indexPath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Method(selector = "tableView:editActionsForRowAtIndexPath:")
	public NSArray<UITableViewRowAction> getEditActionsForRow(
			UITableView tableView, NSIndexPath indexPath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Method(selector = "tableView:shouldIndentWhileEditingRowAtIndexPath:")
	public boolean shouldIndentWhileEditingRow(UITableView tableView,
			NSIndexPath indexPath) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Method(selector = "tableView:willBeginEditingRowAtIndexPath:")
	public void willBeginEditingRow(UITableView tableView, NSIndexPath indexPath) {
		// TODO Auto-generated method stub

	}

	@Override
	@Method(selector = "tableView:didEndEditingRowAtIndexPath:")
	public void didEndEditingRow(UITableView tableView, NSIndexPath indexPath) {
		// TODO Auto-generated method stub

	}

	@Override
	@Method(selector = "tableView:targetIndexPathForMoveFromRowAtIndexPath:toProposedIndexPath:")
	public NSIndexPath getTargetForMove(UITableView tableView,
			NSIndexPath sourceIndexPath,
			NSIndexPath proposedDestinationIndexPath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Method(selector = "tableView:indentationLevelForRowAtIndexPath:")
	@MachineSizedSInt
	public long getIndentationLevelForRow(UITableView tableView,
			NSIndexPath indexPath) {
		return 1;
	}

	@Override
	@Method(selector = "tableView:shouldShowMenuForRowAtIndexPath:")
	public boolean shouldShowMenuForRow(UITableView tableView,
			NSIndexPath indexPath) {
		return false;
	}

	@Override
	@Method(selector = "tableView:canPerformAction:forRowAtIndexPath:withSender:")
	public boolean canPerformAction(UITableView tableView, Selector action,
			NSIndexPath indexPath, NSObject sender) {
		return false;
	}

	@Override
	@Method(selector = "tableView:performAction:forRowAtIndexPath:withSender:")
	public void performActionForRow(UITableView tableView, Selector action,
			NSIndexPath indexPath, NSObject sender) {
	}
}

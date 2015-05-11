package net.mwplay.controller;

import java.util.List;

import net.mwplay.beans.Channel;
import net.mwplay.utils.OnChannelChangListener;
import net.mwplay.utils.PrintTool;

import org.robovm.apple.coregraphics.CGPoint;
import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSArray.AsStringListMarshaler;
import org.robovm.apple.foundation.NSIndexPath;
import org.robovm.apple.foundation.NSObject;
import org.robovm.apple.uikit.UIScrollView;
import org.robovm.apple.uikit.UITableView;
import org.robovm.apple.uikit.UITableViewCell;
import org.robovm.apple.uikit.UITableViewCellEditingStyle;
import org.robovm.apple.uikit.UITableViewDataSource;
import org.robovm.apple.uikit.UITableViewDelegate;
import org.robovm.apple.uikit.UITableViewRowAction;
import org.robovm.apple.uikit.UIView;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.objc.Selector;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.objc.annotation.IBOutlet;
import org.robovm.objc.annotation.Method;
import org.robovm.rt.bro.annotation.ByVal;
import org.robovm.rt.bro.annotation.MachineSizedFloat;
import org.robovm.rt.bro.annotation.MachineSizedSInt;

@CustomClass("ChannelViewController")
public class ChannelViewController extends UIViewController implements UITableViewDataSource, UITableViewDelegate{
	UITableView mTableView;
	
	List<Channel> channels;
	OnChannelChangListener mChangListener;
	
	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}
	
	public void setChangListener(OnChannelChangListener mChangListener) {
		this.mChangListener = mChangListener;
	}
	
	@IBOutlet
	public void setTableView(UITableView mTableView) {
		this.mTableView = mTableView;
	}
	
	@Override
	@Method(selector = "viewDidLoad")
	public void viewDidLoad() {
		super.viewDidLoad();
		mTableView.setDataSource(this);
		mTableView.setDelegate(this);
		
		PrintTool.println("viewDidLoad");
	}
	
	@Override
	@Method(selector = "tableView:numberOfRowsInSection:")
	@MachineSizedSInt
	public long getNumberOfRowsInSection(UITableView tableView,
			@MachineSizedSInt long section) {
		return channels.size();
	}

	@Override
	@Method(selector = "tableView:cellForRowAtIndexPath:")
	public
	UITableViewCell getCellForRow(UITableView tableView, NSIndexPath indexPath) {
		UITableViewCell mCell = tableView.dequeueReusableCell("channel", indexPath);
		
		Channel channel = channels.get((int) indexPath.getRow());
		mCell.getTextLabel().setText(channel.getName());
		
		return mCell;
	}

	@Override
	@Method(selector = "numberOfSectionsInTableView:")
	@MachineSizedSInt
	public
	long getNumberOfSections(UITableView tableView) {
		return 1;
	}

	@Override
	@Method(selector = "tableView:titleForHeaderInSection:")
	public
	String getTitleForHeader(UITableView tableView,
			@MachineSizedSInt long section) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Method(selector = "tableView:titleForFooterInSection:")
	public
	String getTitleForFooter(UITableView tableView,
			@MachineSizedSInt long section) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Method(selector = "tableView:canEditRowAtIndexPath:")
	public
	boolean canEditRow(UITableView tableView, NSIndexPath indexPath) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Method(selector = "tableView:canMoveRowAtIndexPath:")
	public
	boolean canMoveRow(UITableView tableView, NSIndexPath indexPath) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Method(selector = "sectionIndexTitlesForTableView:")
	@org.robovm.rt.bro.annotation.Marshaler(AsStringListMarshaler.class)
	public
	List<String> getSectionIndexTitles(UITableView tableView) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Method(selector = "tableView:sectionForSectionIndexTitle:atIndex:")
	@MachineSizedSInt
	public
	long getSectionForSectionIndexTitle(UITableView tableView, String title,
			@MachineSizedSInt long index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@Method(selector = "tableView:commitEditingStyle:forRowAtIndexPath:")
	public
	void commitEditingStyleForRow(UITableView tableView,
			UITableViewCellEditingStyle editingStyle, NSIndexPath indexPath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Method(selector = "tableView:moveRowAtIndexPath:toIndexPath:")
	public
	void moveRow(UITableView tableView, NSIndexPath sourceIndexPath,
			NSIndexPath destinationIndexPath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Method(selector = "scrollViewDidScroll:")
	public void didScroll(UIScrollView scrollView) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Method(selector = "scrollViewDidZoom:")
	public
	void didZoom(UIScrollView scrollView) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Method(selector = "scrollViewWillBeginDragging:")
	public
	void willBeginDragging(UIScrollView scrollView) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Method(selector = "scrollViewWillEndDragging:withVelocity:targetContentOffset:")
	public
	void willEndDragging(UIScrollView scrollView, @ByVal CGPoint velocity,
			CGPoint targetContentOffset) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Method(selector = "scrollViewDidEndDragging:willDecelerate:")
	public
	void didEndDragging(UIScrollView scrollView, boolean decelerate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Method(selector = "scrollViewWillBeginDecelerating:")
	public
	void willBeginDecelerating(UIScrollView scrollView) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Method(selector = "scrollViewDidEndDecelerating:")
	public
	void didEndDecelerating(UIScrollView scrollView) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Method(selector = "scrollViewDidEndScrollingAnimation:")
	public
	void didEndScrollingAnimation(UIScrollView scrollView) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Method(selector = "viewForZoomingInScrollView:")
	public
	UIView getViewForZooming(UIScrollView scrollView) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Method(selector = "scrollViewWillBeginZooming:withView:")
	public
	void willBeginZooming(UIScrollView scrollView, UIView view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Method(selector = "scrollViewDidEndZooming:withView:atScale:")
	public
	void didEndZooming(UIScrollView scrollView, UIView view,
			@MachineSizedFloat double scale) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Method(selector = "scrollViewShouldScrollToTop:")
	public
	boolean shouldScrollToTop(UIScrollView scrollView) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Method(selector = "scrollViewDidScrollToTop:")
	public
	void didScrollToTop(UIScrollView scrollView) {
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
	public
	void willDisplayHeaderView(UITableView tableView, UIView view,
			@MachineSizedSInt long section) {
	}

	@Override
	@Method(selector = "tableView:willDisplayFooterView:forSection:")
	public
	void willDisplayFooterView(UITableView tableView, UIView view,
			@MachineSizedSInt long section) {
	}

	@Override
	@Method(selector = "tableView:didEndDisplayingCell:forRowAtIndexPath:")
	public
	void didEndDisplayingCell(UITableView tableView, UITableViewCell cell,
			NSIndexPath indexPath) {
	}

	@Override
	@Method(selector = "tableView:didEndDisplayingHeaderView:forSection:")
	public
	void didEndDisplayingHeaderView(UITableView tableView, UIView view,
			@MachineSizedSInt long section) {
	}

	@Override
	@Method(selector = "tableView:didEndDisplayingFooterView:forSection:")
	public
	void didEndDisplayingFooterView(UITableView tableView, UIView view,
			@MachineSizedSInt long section) {
	}

	@Override
	@Method(selector = "tableView:heightForRowAtIndexPath:")
	@MachineSizedFloat
	public
	double getHeightForRow(UITableView tableView, NSIndexPath indexPath) {
		return 40;
	}

	@Override
	@Method(selector = "tableView:heightForHeaderInSection:")
	@MachineSizedFloat
	public
	double getHeightForHeader(UITableView tableView,
			@MachineSizedSInt long section) {
		return 0;
	}

	@Override
	@Method(selector = "tableView:heightForFooterInSection:")
	@MachineSizedFloat
	public
	double getHeightForFooter(UITableView tableView,
			@MachineSizedSInt long section) {
		return 0;
	}

	@Override
	@Method(selector = "tableView:estimatedHeightForRowAtIndexPath:")
	@MachineSizedFloat
	public
	double getEstimatedHeightForRow(UITableView tableView, NSIndexPath indexPath) {
		return 0;
	}

	@Override
	@Method(selector = "tableView:estimatedHeightForHeaderInSection:")
	@MachineSizedFloat
	public
	double getEstimatedHeightForHeader(UITableView tableView,
			@MachineSizedSInt long section) {
		return 0;
	}

	@Override
	@Method(selector = "tableView:estimatedHeightForFooterInSection:")
	@MachineSizedFloat
	public
	double getEstimatedHeightForFooter(UITableView tableView,
			@MachineSizedSInt long section) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@Method(selector = "tableView:viewForHeaderInSection:")
	public
	UIView getViewForHeader(UITableView tableView,
			@MachineSizedSInt long section) {
		return null;
	}

	@Override
	@Method(selector = "tableView:viewForFooterInSection:")
	public
	UIView getViewForFooter(UITableView tableView,
			@MachineSizedSInt long section) {
		return null;
	}

	@Override
	@Method(selector = "tableView:accessoryButtonTappedForRowWithIndexPath:")
	public
	void accessoryButtonTapped(UITableView tableView, NSIndexPath indexPath) {
	}

	@Override
	@Method(selector = "tableView:shouldHighlightRowAtIndexPath:")
	public
	boolean shouldHighlightRow(UITableView tableView, NSIndexPath indexPath) {
		return true;
	}

	@Override
	@Method(selector = "tableView:didHighlightRowAtIndexPath:")
	public
	void didHighlightRow(UITableView tableView, NSIndexPath indexPath) {
	}

	@Override
	@Method(selector = "tableView:didUnhighlightRowAtIndexPath:")
	public
	void didUnhighlightRow(UITableView tableView, NSIndexPath indexPath) {
	}

	@Override
	@Method(selector = "tableView:willSelectRowAtIndexPath:")
	public
	NSIndexPath willSelectRow(UITableView tableView, NSIndexPath indexPath) {
		// TODO Auto-generated method stub
		return indexPath;
	}

	@Override
	@Method(selector = "tableView:willDeselectRowAtIndexPath:")
	public
	NSIndexPath willDeselectRow(UITableView tableView, NSIndexPath indexPath) {
		// TODO Auto-generated method stub
		return indexPath;
	}

	@Override
	@Method(selector = "tableView:didSelectRowAtIndexPath:")
	public
	void didSelectRow(UITableView tableView, NSIndexPath indexPath) {
		int row = (int) indexPath.getRow();
		
		mChangListener.onChannelChanged(channels.get(row).getId() + "");
		
		this.dismissViewController(true, null);
	}

	@Override
	@Method(selector = "tableView:didDeselectRowAtIndexPath:")
	public
	void didDeselectRow(UITableView tableView, NSIndexPath indexPath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Method(selector = "tableView:editingStyleForRowAtIndexPath:")
	public
	UITableViewCellEditingStyle getEditingStyleForRow(UITableView tableView,
			NSIndexPath indexPath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Method(selector = "tableView:titleForDeleteConfirmationButtonForRowAtIndexPath:")
	public
	String getTitleForDeleteConfirmationButton(UITableView tableView,
			NSIndexPath indexPath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Method(selector = "tableView:editActionsForRowAtIndexPath:")
	public
	NSArray<UITableViewRowAction> getEditActionsForRow(UITableView tableView,
			NSIndexPath indexPath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Method(selector = "tableView:shouldIndentWhileEditingRowAtIndexPath:")
	public
	boolean shouldIndentWhileEditingRow(UITableView tableView,
			NSIndexPath indexPath) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Method(selector = "tableView:willBeginEditingRowAtIndexPath:")
	public
	void willBeginEditingRow(UITableView tableView, NSIndexPath indexPath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Method(selector = "tableView:didEndEditingRowAtIndexPath:")
	public
	void didEndEditingRow(UITableView tableView, NSIndexPath indexPath) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@Method(selector = "tableView:targetIndexPathForMoveFromRowAtIndexPath:toProposedIndexPath:")
	public
	NSIndexPath getTargetForMove(UITableView tableView,
			NSIndexPath sourceIndexPath,
			NSIndexPath proposedDestinationIndexPath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Method(selector = "tableView:indentationLevelForRowAtIndexPath:")
	@MachineSizedSInt
	public
	long getIndentationLevelForRow(UITableView tableView, NSIndexPath indexPath) {
		return 1;
	}

	@Override
	@Method(selector = "tableView:shouldShowMenuForRowAtIndexPath:")
	public
	boolean shouldShowMenuForRow(UITableView tableView, NSIndexPath indexPath) {
		return false;
	}

	@Override
	@Method(selector = "tableView:canPerformAction:forRowAtIndexPath:withSender:")
	public
	boolean canPerformAction(UITableView tableView, Selector action,
			NSIndexPath indexPath, NSObject sender) {
		return false;
	}

	@Override
	@Method(selector = "tableView:performAction:forRowAtIndexPath:withSender:")
	public
	void performActionForRow(UITableView tableView, Selector action,
			NSIndexPath indexPath, NSObject sender) {
	}
}

// Custom Hybrid Flyout - Horizontal position, Vertical layout
class HybridFlyout extends Blockly.HorizontalFlyout {
	constructor(workspaceOptions) {
		super(workspaceOptions);
		// Override to use vertical layout behavior
		this.horizontalLayout = false; // This controls block arrangement
	}

	// Override layout to arrange blocks vertically like VerticalFlyout
	layout_(contents) {
		this.workspace_.scale = this.targetWorkspace.scale;
		const margin = this.MARGIN;
		const cursorX = this.RTL ? margin : margin + this.tabWidth_;
		let cursorY = margin;

		for (const item of contents) {
			item.getElement().moveBy(cursorX, cursorY);
			cursorY += item.getElement().getBoundingRectangle().getHeight() + margin;
		}
	}

	// Override reflow to calculate height like VerticalFlyout
	reflowInternal_() {
		this.workspace_.scale = this.getFlyoutScale();
		let flyoutHeight = this.getContents().reduce((maxHeightSoFar, item) => {
			return maxHeightSoFar + item.getElement().getBoundingRectangle().getHeight();
		}, 0);
		flyoutHeight += this.MARGIN * (this.getContents().length + 1);
		flyoutHeight *= this.workspace_.scale;
		flyoutHeight += Blockly.Scrollbar.scrollbarThickness;

		// Set a reasonable width for vertical layout
		const flyoutWidth = 200; // Fixed width for vertical block layout

		if (this.getHeight() !== flyoutHeight || this.getWidth() !== flyoutWidth) {
			this.height_ = flyoutHeight;
			this.width_ = flyoutWidth;
			this.position();
			this.targetWorkspace.resizeContents();
			this.targetWorkspace.recordDragTargets();
		}
	}

	// Override wheel scrolling for vertical behavior
	wheel_(e) {
		const scrollDelta = Blockly.browserEvents.getScrollDeltaPixels(e);

		if (scrollDelta.y) {
			const metricsManager = this.workspace_.getMetricsManager();
			const scrollMetrics = metricsManager.getScrollMetrics();
			const viewMetrics = metricsManager.getViewMetrics();
			const pos = viewMetrics.top - scrollMetrics.top + scrollDelta.y;

			this.workspace_.scrollbar?.setY(pos);
			Blockly.WidgetDiv.hideIfOwnerIsInWorkspace(this.workspace_);
			Blockly.dropDownDiv.hideWithoutAnimation();
		}
		e.preventDefault();
		e.stopPropagation();
	}

	// Keep horizontal positioning but allow vertical scrolling
	isDragTowardWorkspace(currentDragDeltaXY) {
		const dx = currentDragDeltaXY.x;
		const dy = currentDragDeltaXY.y;
		const dragDirection = (Math.atan2(dy, dx) / Math.PI) * 180;
		const range = this.dragAngleRange_;

		// Allow both horizontal and vertical drag directions
		return (dragDirection < 90 + range && dragDirection > 90 - range) || (dragDirection > -90 - range && dragDirection < -90 + range) || (dragDirection < range && dragDirection > -range) || dragDirection < -180 + range || dragDirection > 180 - range;
	}
}

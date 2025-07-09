// 🦖 Simple InnoDino Number Input Popup

window.showInnodinoNumberDialog = function (message, defaultValue, callback) {
	// Get the popup element from HTML
	const popup = document.getElementById("numberInputPopup");
	const messageEl = document.getElementById("numberInputMessage");
	const input = document.getElementById("innodino-number-input");
	const confirmBtn = document.getElementById("numberInputConfirm");
	const cancelBtn = document.getElementById("numberInputCancel");

	if (!popup) {
		console.error("Number input popup not found in HTML");
		callback(null);
		return;
	}

	// Set message and default value
	messageEl.textContent = message;
	input.value = defaultValue || "";

	// Show popup
	popup.style.display = "flex";

	// Focus input
	setTimeout(() => {
		input.focus();
		input.select();
	}, 100);

	// Handle confirm
	const confirm = () => {
		const value = input.value.trim();
		popup.style.display = "none";
		callback(value || null);
		// Clean up event listeners
		cleanup();
	};

	// Handle cancel
	const cancel = () => {
		popup.style.display = "none";
		callback(null);
		// Clean up event listeners
		cleanup();
	};

	// Clean up function to remove event listeners
	const cleanup = () => {
		confirmBtn.removeEventListener("click", confirm);
		cancelBtn.removeEventListener("click", cancel);
		input.removeEventListener("keydown", keyHandler);
		popup.removeEventListener("click", outsideClickHandler);
	};

	// Keyboard handler
	const keyHandler = (e) => {
		if (e.key === "Enter") {
			e.preventDefault();
			confirm();
		} else if (e.key === "Escape") {
			e.preventDefault();
			cancel();
		}
	};

	// Outside click handler
	const outsideClickHandler = (e) => {
		if (e.target === popup) {
			cancel();
		}
	};

	// Event listeners
	confirmBtn.addEventListener("click", confirm);
	cancelBtn.addEventListener("click", cancel);
	input.addEventListener("keydown", keyHandler);
	popup.addEventListener("click", outsideClickHandler);
};

// 🦖 InnoDino Text Input Dialog for Variable Creation
window.showInnodinoTextDialog = function (message, defaultValue, callback) {
	// Get the popup element from HTML
	const popup = document.getElementById("textInputPopup");
	const messageEl = document.getElementById("textInputMessage");
	const input = document.getElementById("innodino-text-input");
	const confirmBtn = document.getElementById("textInputConfirm");
	const cancelBtn = document.getElementById("textInputCancel");

	if (!popup) {
		console.error("Text input popup not found in HTML");
		callback(null);
		return;
	}

	// Set message and default value
	messageEl.textContent = message;
	input.value = defaultValue || "";

	// Show popup
	popup.style.display = "flex";

	// Focus input
	setTimeout(() => {
		input.focus();
		input.select();
	}, 100);

	// Handle confirm
	const confirm = () => {
		const value = input.value.trim();
		popup.style.display = "none";
		callback(value || null);
		// Clean up event listeners
		cleanup();
	};

	// Handle cancel
	const cancel = () => {
		popup.style.display = "none";
		callback(null);
		// Clean up event listeners
		cleanup();
	};

	// Clean up function to remove event listeners
	const cleanup = () => {
		confirmBtn.removeEventListener("click", confirm);
		cancelBtn.removeEventListener("click", cancel);
		input.removeEventListener("keydown", keyHandler);
		popup.removeEventListener("click", outsideClickHandler);
	};

	// Keyboard handler
	const keyHandler = (e) => {
		if (e.key === "Enter") {
			e.preventDefault();
			confirm();
		} else if (e.key === "Escape") {
			e.preventDefault();
			cancel();
		}
	};

	// Outside click handler
	const outsideClickHandler = (e) => {
		if (e.target === popup) {
			cancel();
		}
	};

	// Event listeners
	confirmBtn.addEventListener("click", confirm);
	cancelBtn.addEventListener("click", cancel);
	input.addEventListener("keydown", keyHandler);
	popup.addEventListener("click", outsideClickHandler);
};

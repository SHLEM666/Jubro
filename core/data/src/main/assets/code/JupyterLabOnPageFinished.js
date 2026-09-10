(function (window) {
	if (document.body) {
		try {
			var elem = document.createElement("style");

			// Make main menu panel scrollable
			elem.innerHTML += "#jp-menu-panel {overflow-x: scroll;}";

            // Increase main menu panel height
            elem.innerHTML += ":root {--jp-private-menubar-height: 34px;}";

            // Increase tab bar height
            elem.innerHTML += ":root {--jp-private-horizontal-tab-height: 30px;}";
            elem.innerHTML += ".lm-TabBar-addButton[title^='New Launcher'] {padding: 7.5px;}";

			// Remove unnecessary padding from the main menu panel
			// that appear on the left in front of the File
			// menu button in the Simple interface mode.
			elem.innerHTML += ".jp-LabShell[data-shell-mode='single-document'] #jp-menu-panel {padding: 0px;}";

            // Remove unnecessary padding that appears as the thin grey
            // frame around workspace in default interface mode.
            elem.innerHTML += "#jp-main-dock-panel {padding: 0px;}";

			document.body.appendChild(elem);

			// Cancel the context menu event
			document.addEventListener('contextmenu', (e) => {
				e.stopImmediatePropagation();
				e.stopPropagation();
			}, true);

		} catch (err) {
			alert(err);
		};
	};
})(window);
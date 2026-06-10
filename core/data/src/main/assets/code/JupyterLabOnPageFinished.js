(function (window) {
	//if (/.ipynb/.test(window.location.href)) {
	if (document.body) {
		try {
			var elem = document.createElement("style");

			// Make main menu panel scrollable
			elem.innerHTML += "#jp-menu-panel {overflow-x: scroll;}";

            // Increase main menu panel height
            elem.innerHTML += ":root {--jp-private-menubar-height: 34px;}";

            // Increase tab bar height
            elem.innerHTML += ":root {--jp-private-horizontal-tab-height: 30px;}";

			// Remove unnecessary padding from the main menu panel
			// that appear on the left in front of the File
			// menu button in the Simple interface mode.
			elem.innerHTML += ".jp-LabShell[data-shell-mode='single-document'] #jp-menu-panel {padding: 0px;}";

            // Remove unnecessary padding that appears as the thin grey
            // frame around workspace in default interface mode.
            elem.innerHTML += "#jp-main-dock-panel {padding: 0px;}";

            //elem.innerHTML += "#jp-MainMenu {overflow-x: scroll;}";
			//elem.innerHTML += ".jp-NotebookTrustedStatus {display: none;}";
			//elem.innerHTML += "select {display: none;}";

			document.body.appendChild(elem);

			// Cancel the context menu event in the code editing area
			document.addEventListener('contextmenu', (e) => {
				if (
                    e.target.closest('.jp-Cell')

				    //e.target.closest('.jp-CodeMirrorEditor') ||
				    //e.target.closest('.jp-RenderedMarkdown') ||
                    //e.target.closest('.jp-RenderedText') ||
                    //e.target.closest('.jp-OutputArea') ||
                    //e.target.closest('.jp-InputArea') ||
				) {
					e.stopImmediatePropagation();
					e.stopPropagation();
				}
			}, true);

		} catch (err) {
			alert(err);
		};
	};
})(window);
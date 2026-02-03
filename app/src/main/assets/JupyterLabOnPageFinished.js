(function (window) {
	//if (/.ipynb/.test(window.location.href)) {
	if (document.body) {
		try {
			var elem = document.createElement("style");
			elem.innerHTML += "#jp-MainMenu {overflow-x: scroll;}";
			//elem.innerHTML += ".jp-NotebookTrustedStatus {display: none;}";
			//elem.innerHTML += "select {display: none;}";
			document.body.appendChild(elem);
			document.addEventListener('contextmenu', (e) => {
				if (e.target.closest('.jp-CodeMirrorEditor')) {
					e.stopImmediatePropagation();
					e.stopPropagation();
				}
			}, true);
		} catch (err) {
			alert(err);
		};
	};
})(window);
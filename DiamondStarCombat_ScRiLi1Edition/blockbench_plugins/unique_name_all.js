(function() {
    var button;

    Plugin.register('unique_name_all', {
        title: 'Unique Name All',
        author: '1whohears',
        description: 'Gives All Selected Components a Unique Name',
        icon: 'bar_chart',
        version: '1.0.0',
        variant: 'both',
        onload() {
            button = new Action('rename_all_unique', {
                name: 'Rename All Unique',
                description: 'Give All Selected Components a Unique Name',
                icon: 'bar_chart',
                click: function() {
                    Undo.initEdit({elements: Cube.selected});
					var num = 1;
					const names = [];
					Cube.selected.forEach(cube => {
                        while (names.includes(cube.name)) {
							cube.name += "" + num;
							++num;
						}
						names.push(cube.name);
                    });
					Mesh.selected.forEach(mesh => {
                        while (names.includes(mesh.name)) {
							mesh.name += "" + num;
							++num;
						}
						names.push(mesh.name);
                    });
                    Undo.finishEdit('Rename All Unique');
                }
            });
            MenuBar.addAction(button, 'filter');
        },
        onunload() {
            button.delete();
        }
    });

})();

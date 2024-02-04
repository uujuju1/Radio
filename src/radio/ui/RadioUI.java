package radio.ui;

import arc.*;
import mindustry.*;

public class RadioUI {
	public static void load() {
		Vars.ui.settings.addCategory(Core.bundle.get("mod.radio.settings"), "radio-settings-icon", cont -> {
			cont.image(Core.atlas.find("radio-settings-title")).row();

			cont.button(b -> b.add("Playlist Selector"), () -> new RadioPlaylistSelectorDialog("Playlist Selector").show()).growX().row();
			cont.button(b -> b.add("Settings"), () -> new RadioBaseSettingsDialog("Settings").show()).growX();
		});
	}
}

package radio.ui;

import arc.*;
import mindustry.*;

public class RadioUI {
	public static void load() {
		Vars.ui.settings.addCategory(Core.bundle.get("mod.radio-settings"), "radio-settings-icon", cont -> {
			cont.image(Core.atlas.find("radio-settings-title")).row();

			cont.button(b -> b.add(Core.bundle.get("radio.playlist")), () -> new RadioPlaylistSelectorDialog(Core.bundle.get("radio.playlist")).show()).growX().row();
			cont.button(b -> b.add(Core.bundle.get("radio.settings")), () -> new RadioBaseSettingsDialog(Core.bundle.get("radio.menu-settings")).show()).growX();
		});
	}
}

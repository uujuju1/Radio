package radio;

import arc.*;
import mindustry.*;
import mindustry.game.*;
import mindustry.mod.*;
import radio.core.*;

public class RadioMod extends Mod{
	public RadioMod() {
		Events.on(EventType.ClientLoadEvent.class, e -> {
			Vars.control.sound = new RadioSoundControl();
			RadioSettings.load();
		});
	}
}

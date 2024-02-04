package radio.io;

import arc.*;
import arc.struct.*;
import mindustry.*;
import mindustry.gen.*;
import radio.core.*;

public class RadioIO {
	public static RadioSoundControl soundControl = (RadioSoundControl) Vars.control.sound;

	public static void writeSettings() {
		Core.settings.put("radio-menu-music", soundControl.musics.findKey(soundControl.menuMusic, false));
		Core.settings.put("radio-editor-music", soundControl.musics.findKey(soundControl.editorMusic, false));
		Core.settings.put("radio-launch-music", soundControl.musics.findKey(soundControl.launchMusic, false));

		Core.settings.put("radio-playlist-list", soundControl.currentPlaylist.musicNames());
	}

	public static void readSettings() {
		soundControl.menuMusic = soundControl.musics.get(Core.settings.getString("radio-menu-music", ""), Musics.menu);
		soundControl.editorMusic = soundControl.musics.get(Core.settings.getString("radio-editor-music", ""), Musics.editor);
		soundControl.launchMusic = soundControl.musics.get(Core.settings.getString("radio-launch-music", ""), Musics.launch);
		Seq<String> newList = Seq.with(Core.settings.getString("radio-playlist-list", "").split(","));
		if (newList.size > 1) {
			newList.pop();
		} else {
			newList.clear();
		}
		soundControl.currentPlaylist.list = newList.map(string -> soundControl.musics.get(string, Musics.game1));
	}
}

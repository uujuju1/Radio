package radio.ui;

import arc.audio.*;
import arc.func.*;
import arc.graphics.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import radio.core.*;
import radio.io.*;

public class RadioBaseSettingsDialog extends BaseDialog {
	public RadioBaseSettingsDialog(String title) {
		super(title);
    cont.clear();
		RadioSoundControl soundControl = (RadioSoundControl) Vars.control.sound;
		ObjectMap<String, Music> musicNames = soundControl.musics;

		cont.table(((TextureRegionDrawable) Tex.whiteui).tint(Color.valueOf("141414")), radio -> {
			radio.add(selectMusicType("Menu Music", musicNames, music -> soundControl.menuMusic = music, musicNames.findKey(soundControl.menuMusic, false))).margin(20).pad(10).growX();
			radio.add(selectMusicType("Editor Music", musicNames, music -> soundControl.editorMusic = music, musicNames.findKey(soundControl.editorMusic, false))).margin(20).pad(10).growX().row();
			radio.add(selectMusicType("Launch Music", musicNames, music -> soundControl.launchMusic = music, musicNames.findKey(soundControl.launchMusic, false))).margin(20).pad(10).growX();
		});
		addCloseButton();
		hidden(RadioIO::writeSettings);
	}

	public Table selectMusicType(String label, ObjectMap<String, Music> musicNames, Cons<Music> cons, String current) {
		RadioSoundControl soundControl = (RadioSoundControl) Vars.control.sound;
		return new Table(((TextureRegionDrawable) Tex.whiteui).tint(Color.valueOf("202020")), selector -> {
			Table currentMusic = new Table(Styles.flatDown, t -> t.add(current));
			selector.add(label).color(Pal.accent).left().padBottom(20).row();
			ScrollPane pane = selector.pane(Styles.smallPane, musics -> {
				musicNames.each((musicName, music) -> {
					musics.button(b -> b.add(musicName), () -> {
						soundControl.stop();
						soundControl.playOnce(music);
						currentMusic.clear();
						currentMusic.add(musicName);
						cons.get(music);
					}).height(50).growX().row();
				});
			}).maxHeight(150).get();
			pane.setScrollingDisabled(true, false);
			pane.setOverscroll(false, false);
			selector.row();
			selector.image(Tex.underline).growX().pad(10).row();
			selector.table(actions -> {
				actions.left();
				actions.button(Icon.pause, soundControl::stop).size(50);
				actions.add(currentMusic).height(50).growX().padLeft(10);
			}).growX();
		});
	}
}

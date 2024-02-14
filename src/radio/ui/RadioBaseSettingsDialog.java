package radio.ui;

import arc.audio.*;
import arc.scene.style.*;
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
		ObjectMap.Entry<String, Music> current = new ObjectMap.Entry<>();
		Table currentTable = new Table(Styles.black);
		current.value = new Music();
		current.key = "";

		cont.table(((TextureRegionDrawable) Tex.whiteui).tint(Pal.darkestGray), radio -> {
			radio.table(Styles.black3, selector -> selector.pane(Styles.smallPane, select -> {
				select.button(b -> b.add("None"), () -> {
					current.key = "";
					current.value = new Music();

					currentTable.clear();
				}).height(50).minWidth(250).row();
				soundControl.musics.each((name, music) -> select.button(b -> b.add(name), () -> {
					current.key = name;
					current.value = music;

					currentTable.clear();
					currentTable.add(name);
				}).height(50).minWidth(250).row());
			}).maxHeight(250).pad(10).get()).margin(10).row();

			radio.table(Tex.button, b -> b.add(currentTable)).growX().height(50).padTop(20).padBottom(10).row();
			radio.table(actions -> {
				actions.left();
				actions.button(Icon.play, () -> {
					soundControl.stop();
					soundControl.playOnce(current.value);
				}).size(50, 50);
				actions.button(Icon.pause, soundControl::stop).size(50, 50).padRight(25);

				actions.button(Icon.home, () -> soundControl.menuMusic = current.value).size(50, 50).tooltip(tooltip -> {
					tooltip.background(Tex.button);
					tooltip.add("Menu");
				});
				actions.button(Icon.terrain, () -> soundControl.editorMusic = current.value).size(50, 50).tooltip(tooltip -> {
					tooltip.background(Tex.button);
					tooltip.add("Editor");
				});
				actions.button(Icon.planet, () -> soundControl.launchMusic = current.value).size(50, 50).tooltip(tooltip -> {
					tooltip.background(Tex.button);
					tooltip.add("Campaign");
				});
      }).growX();
		}).margin(10);
		addCloseButton();
		hidden(RadioIO::writeSettings);
	}

//	public Table selectMusicType(String label, ObjectMap<String, Music> musicNames, Cons<Music> cons, String current) {
//		RadioSoundControl soundControl = (RadioSoundControl) Vars.control.sound;
//		return new Table(((TextureRegionDrawable) Tex.whiteui).tint(Color.valueOf("202020")), selector -> {
//			Table currentMusic = new Table(Styles.flatDown, t -> t.add(current));
//			selector.add(label).color(Pal.accent).left().padBottom(20).row();
//			ScrollPane pane = selector.pane(Styles.smallPane, musics -> {
//				musicNames.each((musicName, music) -> {
//					musics.button(b -> b.add(musicName), () -> {
//						soundControl.stop();
//						soundControl.playOnce(music);
//						currentMusic.clear();
//						currentMusic.add(musicName);
//						cons.get(music);
//					}).height(50).growX().row();
//				});
//			}).maxHeight(150).get();
//			pane.setScrollingDisabled(true, false);
//			pane.setOverscroll(false, false);
//			selector.row();
//			selector.image(Tex.underline).growX().pad(10).row();
//			selector.table(actions -> {
//				actions.left();
//				actions.button(Icon.pause, soundControl::stop).size(50);
//				actions.add(currentMusic).height(50).growX().padLeft(10);
//			}).growX();
//		});
//	}
}

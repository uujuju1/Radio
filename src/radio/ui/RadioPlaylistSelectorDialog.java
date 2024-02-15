package radio.ui;

import arc.audio.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.Button.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import radio.core.*;
import radio.io.*;

public class RadioPlaylistSelectorDialog extends BaseDialog {
	public RadioPlaylistSelectorDialog(String title) {
		super(title);
		cont.clear();
		RadioSoundControl soundControl = (RadioSoundControl) Vars.control.sound;
		Seq<Music> playlistList = new Seq<>();
		Table playlistTable = new Table(Styles.black);
		Table selectTable = new Table();
		var ref = new Object() {
			Music selectMusic = new Music();
		};

		ButtonStyle style = new ButtonStyle() {{
			down = Tex.sidelineOver;
			up = Tex.sideline;
			over = Tex.sidelineOver;
		}};

		cont.table(((TextureRegionDrawable) Tex.whiteui).tint(Pal.darkestGray), playlist -> {
			playlist.table(radio -> {
				radio.table(Styles.black3, selector -> selector.pane(Styles.smallPane, select -> soundControl.musics.each((name, music) -> select.button(b -> b.add(name), () -> {
					Button v = playlistTable.button(b -> b.add(name), style, () -> {}).growX().get();
					playlistList.add(music);
					v.clicked(() -> {
						playlistTable.removeChild(v);
						playlistList.remove(music);
					});
					selectTable.clear();
					selectTable.add(name).color(Pal.accent);
					ref.selectMusic = music;
					playlistTable.row();
				}).height(50).minWidth(250).row())).maxHeight(250)).margin(10).padRight(10);

				radio.table(Styles.black3, selector -> selector.pane(Styles.smallPane, playlistTable).maxHeight(250).minWidth(250).pad(10)).height(270).row();
      }).row();

			playlist.image(Tex.whiteui).growX().padBottom(10).padTop(10).color(Pal.accent).row();
			playlist.table(Styles.black6, display -> display.add(selectTable)).growX().height(50).row();

			playlist.table(actions -> {
				actions.table(music -> {
					music.left();
					music.button(Icon.play, () -> {
						soundControl.stop();
						soundControl.playOnce(ref.selectMusic);
					}).size(50);
					music.button(Icon.pause, soundControl::stop).size(50);
		    }).growX().padTop(10);

				actions.table(list -> {
					list.right();
					list.button(Icon.cancel, () -> {
						playlistList.clear();
						playlistTable.clear();
					}).size(50);
					list.button(Icon.upload, () -> soundControl.currentPlaylist = new RadioSoundControl.Playlist(playlistList, 0, true)).size(50);
		    }).growX();
		  }).growX();
		}).margin(10);
		addCloseButton();
		hidden(RadioIO::writeSettings);
	}
}

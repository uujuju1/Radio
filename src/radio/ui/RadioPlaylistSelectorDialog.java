package radio.ui;

import arc.audio.*;
import arc.graphics.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import radio.core.*;
import radio.io.*;

public class RadioPlaylistSelectorDialog extends BaseDialog {
	public RadioPlaylistSelectorDialog(String title) {
		super(title);
		cont.clear();
		RadioSoundControl soundControl = (RadioSoundControl) Vars.control.sound;
		ObjectMap<String, Music> musicNames = soundControl.musics;
		Seq<Music> playlistS = new Seq<>();
		Table playlistTable = new Table();

		cont.table(((TextureRegionDrawable) Tex.whiteui).tint(Color.valueOf("141414")), radio -> {
			Table selectTable = radio.table(((TextureRegionDrawable) Tex.whiteui).tint(Color.valueOf("202020")), selector -> {
				ScrollPane selectorPane = new ScrollPane(new Table(musics -> musicNames.each((musicName, music) -> {
					musics.button(button -> button.add(musicName), () -> {
						Button button = playlistTable.button(b -> b.add(musicName), () -> {
						}).growX().get();
						soundControl.stop();
						soundControl.playOnce(music);
						button.clicked(() -> {
							playlistTable.removeChild(button);
							playlistS.remove(music);
						});
						playlistS.add(music);
						playlistTable.row();
					}).height(50).growX().row();
				})), Styles.smallPane);
				selectorPane.setScrollingDisabled(true, false);
				selectorPane.setOverscroll(false, false);
				selector.add(selectorPane).maxHeight(400).row();
				selector.image(Tex.underline).growX().row();

				selector.table(actions -> {
					actions.left();
					actions.button(Icon.upload, () -> {
						soundControl.stop();
						soundControl.currentPlaylist = new RadioSoundControl.Playlist(playlistS.copy(), 0, true);
						//soundControl.ambientMusic = Seq.with(playlistS);
						playlistS.clear();
						playlistTable.clear();
					}).size(50);
					actions.button(Icon.cancel, () -> {
						soundControl.stop();
						playlistS.clear();
						playlistTable.clear();
					}).size(50);
					actions.button(Icon.play, soundControl::stop).size(50);
				}).growX().padTop(20);
			}).margin(20).padRight(20).get();

			radio.table(((TextureRegionDrawable) Tex.whiteui).tint(Color.valueOf("202020")), playlist -> {
				ScrollPane pane = new ScrollPane(playlistTable);
				playlist.add(pane).maxHeight(selectTable.getPrefHeight() - 40);
			}).growY().width(selectTable.getPrefWidth());
		}).margin(20).row();
		addCloseButton();
		hidden(RadioIO::writeSettings);
	}
}

package radio.core;

import arc.*;
import arc.audio.*;
import arc.graphics.*;
import arc.scene.style.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.ui.*;
import radio.core.RadioSoundControl.*;
import radio.ui.*;

public class RadioSettings {
	public static void load() {
		RadioSoundControl soundControl = (RadioSoundControl) Vars.control.sound;
		Vars.ui.settings.addCategory(Core.bundle.get("mod.radio.settings"), "radio-settings-icon", cont -> {
			cont.image(Core.atlas.find("radio-settings-title")).row();
      Seq<Music> playlistS = new Seq<>();
 			Table playlistTable = new Table();

			cont.table(((TextureRegionDrawable) Tex.whiteui).tint(Color.valueOf("141414")), radio -> {
				Table selectTable = radio.table(((TextureRegionDrawable) Tex.whiteui).tint(Color.valueOf("202020")), selector -> {
					ScrollPane selectorPane = new ScrollPane(new Table(musics -> soundControl.musics().each(music -> {
						musics.button(button -> button.add(music.toString()), () -> {
							Button button = playlistTable.button(b -> b.add(music.toString()), () -> {
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
							soundControl.currentPlaylist = new Playlist(playlistS.copy(), 0, true);
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
			}).margin(20);

			cont.button("radio", () -> new RadioDialog("a").show());
		});
	}
}

package radio.ui;

import arc.audio.*;
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

public class RadioDialog extends BaseDialog {
	public RadioDialog(String title) {
		super("Radio");
		cont.clear();
		cont.table(Styles.black, table -> {
			RadioSoundControl soundControl = ((RadioSoundControl) Vars.control.sound);
			Seq<Music> musics = new Seq<>();
			Table
				selectTable = new Table(),
				playlistTable = new Table(((TextureRegionDrawable) Tex.whiteui).tint(Pal.darkOutline));

			soundControl.musics().each(music -> selectTable.button(b -> b.add(music.toString()), () -> {
				soundControl.stop();
				soundControl.playOnce(music);
				playlistTable.add(music.toString()).size(100, 45).color(Pal.accent).row();
				musics.add(music);
			}).growX().row());

			ScrollPane
				selectPane = new ScrollPane(selectTable, Styles.smallPane),
				playlistPane = new ScrollPane(playlistTable, Styles.smallPane);
			selectPane.setScrollingDisabled(true, false);
			selectPane.setOverscroll(false, false);
			playlistPane.setScrollingDisabled(true, false);
			playlistPane.setOverscroll(false, false);

			table.table(selector -> {
				selector.add(selectPane).maxHeight(180).row();

				selector.table(actions -> {
					actions.button(Icon.pause, soundControl::stop).size(50);
					actions.button(Icon.cancel, () -> {
						soundControl.stop();
						playlistTable.clear();
						musics.clear();
					}).size(50);
					actions.button(Icon.upload, () -> {
						soundControl.stop();
						playlistTable.clear();
						soundControl.ambientMusic.clear();
						soundControl.ambientMusic.add(musics);
						musics.clear();
					}).size(50).row();
        });
			});
			table.add(playlistPane).maxHeight(180).size(200, 225);
		});
		addCloseButton();
	}
}

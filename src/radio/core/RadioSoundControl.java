package radio.core;

import arc.*;
import arc.audio.*;
import arc.files.*;
import arc.struct.*;
import mindustry.*;
import mindustry.audio.*;
import mindustry.gen.*;

import static mindustry.Vars.*;

public class RadioSoundControl extends SoundControl {
	public Playlist currentPlaylist = new Playlist(Seq.with(), 0, false);

	public RadioSoundControl() {
		musicChance = 0;
		musicWaveChance = 0;
	}

	public boolean silenced() {
		return silenced;
	}
	public void silenced(boolean silenced) {
		this.silenced = silenced;
	}

	public Music current() {
		return current;
	}
	public void current(Music music) {
		current = music;
	}

	@Override public boolean isDark() {
		return super.isDark();
	}
	@Override public void play(Music music) {
		if (music != null) silenced = false;
		super.play(music);
	}
	@Override public void playOnce(Music music) {
		super.playOnce(music);
	}
	@Override public void setupFilters() {
		super.setupFilters();
	}
	@Override public void reload() {
		super.reload();
	}
	@Override public void updateLoops() {
		super.updateLoops();
	}
	@Override public boolean shouldPlay() {
		return super.shouldPlay();
	}
	@Override public void silence() {
		super.silence();
	}

	public Seq<Music> musics() {
		Seq<Music> out = new Seq<>();

		Vars.mods.orderedMods().each(mod -> new ZipFi(mod.file).child("music").seq().each(music -> {
			try {
				out.add(new Music(music));
			} catch (Exception horrible) {
				throw new RuntimeException("ah yes", horrible);
			}
		}));

		out.addAll(
			Musics.boss1, Musics.boss2, Musics.editor, Musics.fine,
			Musics.game1, Musics.game2, Musics.game3, Musics.game4,
			Musics.game5, Musics.game6, Musics.game7, Musics.game8,
			Musics.game9, Musics.land, Musics.launch, Musics.menu
		);
		return out;
	}

	@Override
	public void update() {
		boolean paused = state.isGame() && Core.scene.hasDialog();
		boolean playing = state.isGame();

		//check if current track is finished
		if(current != null && !current.isPlaying()){
			current = null;
			fade = 0f;
		}

		//fade the lowpass filter in/out, poll every 30 ticks just in case performance is an issue
		if(timer.get(1, 30f)){
			Core.audio.soundBus.fadeFilterParam(0, Filters.paramWet, paused ? 1f : 0f, 0.4f);
		}

		//play/stop ordinary effects
		if(playing != wasPlaying){
			wasPlaying = playing;

			if(playing){
				Core.audio.soundBus.play();
				setupFilters();
			}else{
				//stopping a single audio bus stops everything else, yay!
				Core.audio.soundBus.stop();
				//play music bus again, as it was stopped above
				Core.audio.musicBus.play();

				Core.audio.soundBus.play();
			}
		}

		Core.audio.setPaused(Core.audio.soundBus.id, state.isPaused());

		if (current == null && !currentPlaylist.ended()) {
			playOnce(currentPlaylist.nextMusic());
		}

		updateLoops();
	}

	public static class Playlist {
		public Seq<Music> list;
		public int current;
		public boolean repeats;

		public Playlist(Seq<Music> list, int current, boolean repeats) {
			this.list = list;
			this.current = current;
			this.repeats = repeats;
		}

		public Music nextMusic() {
			Music out = list.find(music -> list.indexOf(music) == current);
			current++;
			if (repeats && current >= list.size) current = 0;
			return out;
		}

		public boolean ended() {
			return !repeats || current >= list.size;
		}
	}
}

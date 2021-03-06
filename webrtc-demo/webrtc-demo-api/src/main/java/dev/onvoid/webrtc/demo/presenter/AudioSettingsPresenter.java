/*
 * Copyright 2019 Alex Andres
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.onvoid.webrtc.demo.presenter;

import dev.onvoid.webrtc.demo.config.AudioConfiguration;
import dev.onvoid.webrtc.demo.config.Configuration;
import dev.onvoid.webrtc.demo.view.AudioSettingsView;
import dev.onvoid.webrtc.media.audio.AudioDevice;
import dev.onvoid.webrtc.media.audio.AudioDeviceModule;

import java.util.List;

import javax.inject.Inject;

public class AudioSettingsPresenter extends Presenter<AudioSettingsView> {

	private final AudioConfiguration config;


	@Inject
	AudioSettingsPresenter(AudioSettingsView view, Configuration config) {
		super(view);

		this.config = config.getAudioConfiguration();
	}

	@Override
	public void initialize() {
		AudioDeviceModule audioModule = new AudioDeviceModule();

		var playoutDevices = audioModule.getPlayoutDevices();
		var recordingDevices = audioModule.getRecordingDevices();

		var playoutDevice = getAudioDevice(playoutDevices, config.getPlayoutDevice());
		var recordingDevice = getAudioDevice(recordingDevices, config.getRecordingDevice());

		config.setPlayoutDevice(playoutDevice);
		config.setRecordingDevice(recordingDevice);

		view.setAudioPlayoutDevices(playoutDevices);
		view.setAudioRecordingDevices(recordingDevices);
		view.setAudioPlayoutDevice(config.playoutDeviceProperty());
		view.setAudioRecordingDevice(config.recordingDeviceProperty());
		view.setReceiveAudio(config.receiveAudioProperty());
		view.setSendAudio(config.sendAudioProperty());

		audioModule.dispose();
	}

	private AudioDevice getAudioDevice(List<AudioDevice> devices, AudioDevice device) {
		return devices.stream()
				.filter(dev -> dev.equals(device))
				.findFirst()
				.orElse(devices.get(0));
	}
}

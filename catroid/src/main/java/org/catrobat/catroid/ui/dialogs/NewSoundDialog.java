/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2016 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.catrobat.catroid.BuildConfig;
import org.catrobat.catroid.R;
import org.catrobat.catroid.ui.fragment.SoundFragment;
import org.catrobat.catroid.utils.TextSizeUtil;

public class NewSoundDialog extends DialogFragment {

	public static final String TAG = "dialog_new_sound";

	private SoundFragment fragment = null;
	private DialogInterface.OnDismissListener onDismissListener;

	public static NewSoundDialog newInstance() {
		return new NewSoundDialog();
	}

	public void showDialog(Fragment fragment) {
		if (!(fragment instanceof SoundFragment)) {
			throw new RuntimeException("This dialog (NewSoundDialog) can only be called by the SoundFragment.");
		}
		this.fragment = (SoundFragment) fragment;
		show(fragment.getActivity().getFragmentManager(), TAG);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View dialogView = LayoutInflater.from(getActivity())
				.inflate(R.layout.dialog_new_sound, (ViewGroup) getView(), false);
		setupRecordButton(dialogView);
		setupGalleryButton(dialogView);
		setupMediaLibraryButton(dialogView);
		setupPocketMusicButton(dialogView);

		AlertDialog.Builder dialogBuilder = new CustomAlertDialogBuilder(getActivity()).setView(dialogView).setTitle(
				R.string.new_sound_dialog_title);

		final AlertDialog dialog = createDialog(dialogBuilder);
		dialog.setCanceledOnTouchOutside(true);

		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialogInterface) {
				if (getActivity() == null) {
					Log.e(TAG, "onShow() Activity was null!");
					return;
				}
				TextSizeUtil.enlargeViewGroup((ViewGroup) dialog.getWindow().getDecorView().getRootView());
			}
		});
		return dialog;
	}

	public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
		this.onDismissListener = listener;
	}

	@Override
	public void onDismiss(final DialogInterface dialog) {
		super.onDismiss(dialog);
		if (onDismissListener != null) {
			onDismissListener.onDismiss(dialog);
			dialog.dismiss();
		}
	}

	private AlertDialog createDialog(AlertDialog.Builder dialogBuilder) {
		return dialogBuilder.create();
	}

	private void setupRecordButton(View parentView) {
		View recordButton = parentView.findViewById(R.id.dialog_new_sound_recorder);

		recordButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				fragment.addSoundRecord();
				NewSoundDialog.this.dismiss();
			}
		});
	}

	private void setupGalleryButton(View parentView) {
		View galleryButton = parentView.findViewById(R.id.dialog_new_sound_gallery);

		galleryButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				fragment.addSoundChooseFile();
				NewSoundDialog.this.dismiss();
			}
		});
	}

	private void setupMediaLibraryButton(View parentView) {
		View mediaLibraryButton = parentView.findViewById(R.id.dialog_new_sound_media_library);

		mediaLibraryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				fragment.addSoundMediaLibrary();
				NewSoundDialog.this.dismiss();
			}
		});
	}

	private void setupPocketMusicButton(View parentView) {
		View pocketMusicButton = parentView.findViewById(R.id.dialog_new_sound_pocketmusic);

		if (BuildConfig.FEATURE_POCKETMUSIC_ENABLED) {
			pocketMusicButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					fragment.addPocketMusic();
					NewSoundDialog.this.dismiss();
				}
			});
		} else {
			pocketMusicButton.setVisibility(View.GONE);
		}
	}
}

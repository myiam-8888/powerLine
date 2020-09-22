/*
 * Copyright (C) 2014-2020 Arpit Khurana <arpitkh96@gmail.com>, Vishal Nehra <vishalmeham2@gmail.com>,
 * Emmanuel Messulam<emmanuelbendavid@gmail.com>, Raymond Lai <airwave209gt at gmail.com> and Contributors.
 *
 * This file is part of Amaze File Manager.
 *
 * Amaze File Manager is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.amaze.filemanager.database;

import static com.amaze.filemanager.ui.fragments.preference_fragments.PreferencesConstants.PREFERENCE_SORTBY_ONLY_THIS;

import java.util.HashSet;
import java.util.Set;

import com.amaze.filemanager.database.models.explorer.Sort;
import com.inspection.powerline.application.BaseApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/** Created by Ning on 5/28/2018. */
public class SortHandler {

  private final ExplorerDatabase database;

  private SortHandler(@NonNull ExplorerDatabase explorerDatabase) {
    database = explorerDatabase;
  }

  private static class SortHandlerHolder {
    private static final SortHandler INSTANCE =
        new SortHandler(BaseApplication.getInstance().getExplorerDatabase());
  }

  public static SortHandler getInstance() {
    return SortHandlerHolder.INSTANCE;
  }

  public static int getSortType(Context context, String path) {
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    final Set<String> onlyThisFloders =
        sharedPref.getStringSet(PREFERENCE_SORTBY_ONLY_THIS, new HashSet<>());
    final boolean onlyThis = onlyThisFloders.contains(path);
    final int globalSortby = Integer.parseInt(sharedPref.getString("sortby", "0"));
    if (!onlyThis) {
      return globalSortby;
    }
    Sort sort = SortHandler.getInstance().findEntry(path);
    if (sort == null) {
      return globalSortby;
    }
    return sort.type;
  }

  public void addEntry(Sort sort) {
    BaseApplication.runInBackground(() -> database.sortDao().insert(sort));
  }

  public void clear(String path) {
    BaseApplication.runInBackground(() -> database.sortDao().clear(path));
  }

  public void updateEntry(Sort oldSort, Sort newSort) {
    BaseApplication.runInBackground(() -> database.sortDao().update(newSort));
  }

  @Nullable
  public Sort findEntry(String path) {
    return database.sortDao().find(path);
  }
}
package com.zsgame.zonesshift.gamestates.searchlvl;

import java.util.List;
import java.util.Map;

public interface OnMapSearchResult {
    void onSearchComplete(List<Map<String, Object>> maps);
}


package com.community.tool_library.services;

import com.community.tool_library.dtos.WaitlistEntryDTO;

import java.util.List;

public interface WaitlistService {

    WaitlistEntryDTO placeHold(Long itemId, Long userId);

    void removeHold(Long itemId, Long userId);

    void notifyNextUser(Long itemId);

    List<WaitlistEntryDTO> getWaitlistForItem(Long itemId);
}

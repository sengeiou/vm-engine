package com.vm.movie.service.impl;

import com.vm.base.util.BaseService;
import com.vm.dao.util.BasePo;
import com.vm.dao.util.PageBean;
import com.vm.dao.util.QuickSelectOne;
import com.vm.movie.dao.mapper.VmTagsGroupsMapper;
import com.vm.movie.dao.mapper.VmTagsMapper;
import com.vm.movie.dao.mapper.custom.CustomVmTagsGroupsMapper;
import com.vm.movie.dao.po.VmTagsGroups;
import com.vm.movie.dao.qo.VmTagGroupsQueryBean;
import com.vm.movie.service.dto.VmTagsGroupsDto;
import com.vm.movie.service.exception.VmTagGroupsException;
import com.vm.movie.service.inf.VmTagGroupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by ZhangKe on 2017/12/11.
 */
@Service
public class VmTagGroupsServiceImpl extends BaseService implements VmTagGroupsService {
    @Autowired
    private VmTagsMapper vmTagsMapper;
    @Autowired
    private VmTagsGroupsMapper vmTagsGroupsMapper;
    @Autowired
    private CustomVmTagsGroupsMapper customVmTagsGroupsMapper;

    @Override
    public List<VmTagsGroupsDto> getTagsGroupsWithTags() {
        return customVmTagsGroupsMapper.getTagsGroupsWithTags().stream().map((tagGroup) -> {
            VmTagsGroupsDto vmTagsGroupsDto = new VmTagsGroupsDto();
            vmTagsGroupsDto.setId(tagGroup.getId());
            vmTagsGroupsDto.setName(tagGroup.getName());
            vmTagsGroupsDto.setItems(tagGroup.getItems());
            return vmTagsGroupsDto;
        }).collect(toList());
    }


    @Override
    public List<VmTagsGroupsDto> getTagGroups(VmTagGroupsQueryBean query, PageBean page) {
        return customVmTagsGroupsMapper.getTagGroups(query, page).stream().parallel().map(vmTagsGroups -> {
            return makeBackendTagGroupDto(vmTagsGroups);
        }).collect(toList());
    }

    @Override
    public Long getTagGroupsTotal(VmTagGroupsQueryBean query, PageBean page) {
        return customVmTagsGroupsMapper.getTagGroupsTotal(query, page);
    }

    @Override
    public VmTagsGroupsDto editTagGroup(VmTagsGroupsDto vmTagsGroupsDto) {
        VmTagsGroups vmTagsGroups = makeEditTagGroup(vmTagsGroupsDto);
        if (1 != vmTagsGroupsMapper.update(vmTagsGroups.getId(), vmTagsGroups)) {
            throw new VmTagGroupsException("editTagGroup vmTagsGroupsMapper#update is fail ! vmTagsGroupsDto is : " + vmTagsGroupsDto);
        }
        vmTagsGroups = this.getTagGroupById(vmTagsGroups.getId(), BasePo.IsDeleted.NO);
        return makeBackendTagGroupDto(vmTagsGroups);
    }

    private VmTagsGroups makeEditTagGroup(VmTagsGroupsDto vmTagsGroupsDto) {
        VmTagsGroups vmTagsGroups = new VmTagsGroups();
        Integer now = now();
        vmTagsGroups.setName(vmTagsGroupsDto.getName());
        vmTagsGroups.setId(vmTagsGroupsDto.getId());
        vmTagsGroups.setStatus(vmTagsGroupsDto.getStatus());
        vmTagsGroups.setUpdateTime(now);
        return vmTagsGroups;
    }

    private VmTagsGroups getTagGroupById(Long id, BasePo.IsDeleted isDeleted) {
        return QuickSelectOne.getObjectById(vmTagsGroupsMapper, id, isDeleted);
    }

    private VmTagsGroups getTagGroupById(Long id, BasePo.Status status, BasePo.IsDeleted isDeleted) {
        return QuickSelectOne.getObjectById(vmTagsGroupsMapper, id, status, isDeleted);
    }

    @Override
    public VmTagsGroupsDto addTagGroup(VmTagsGroupsDto vmTagsGroupsDto) {
        return null;
    }


    private VmTagsGroupsDto makeBackendTagGroupDto(VmTagsGroups vmTagsGroups) {
        VmTagsGroupsDto vmTagsGroupsDto = new VmTagsGroupsDto();
        vmTagsGroupsDto.setId(vmTagsGroups.getId());
        vmTagsGroupsDto.setName(vmTagsGroups.getName());
        vmTagsGroupsDto.setCreateTime(vmTagsGroups.getCreateTime());
        vmTagsGroupsDto.setUpdateTime(vmTagsGroups.getUpdateTime());
        vmTagsGroupsDto.setStatus(vmTagsGroups.getStatus());
        return vmTagsGroupsDto;
    }


}

package com.shashankbhat.splitbill.service.impl;

import com.shashankbhat.splitbill.dto.bill.*;
import com.shashankbhat.splitbill.dto.user.UserDto;
import com.shashankbhat.splitbill.entity.BillEntity;
import com.shashankbhat.splitbill.entity.BillShareEntity;
import com.shashankbhat.splitbill.entity.UserProfileEntity;
import com.shashankbhat.splitbill.exception.KnownException;
import com.shashankbhat.splitbill.repository.BillRepository;
import com.shashankbhat.splitbill.repository.BillShareRepository;
import com.shashankbhat.splitbill.service.IBillService;
import com.shashankbhat.splitbill.service.IUserProfileService;
import com.shashankbhat.splitbill.util.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BillServiceImpl implements IBillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillShareRepository billShareRepository;

    @Autowired
    private IUserProfileService userProfileService;


    @Override
    public BillSaveDetailDto saveBill(BillSaveDto transaction, Integer uniqueId) throws KnownException {
        BillEntityDto billEntityDto = transaction.getBill();
        Valid<BillEntity> billResult = BillEntity.create(null, billEntityDto.getGroupId(), billEntityDto.getName(),
                billEntityDto.getTotalAmount(), System.currentTimeMillis(), uniqueId);

        if (billResult.isFailed()) {
            throw new KnownException(billResult.getMessage());
        }

        List<BillShareEntity> billShares = new ArrayList<>();
        for (BillShareEntityDto billShareDto : transaction.getBillShares()) {
            Valid<BillShareEntity> billShare = BillShareEntity.create(null, null,
                    billShareDto.getUserId(), billShareDto.getSpent(), billShareDto.getShare(),
                    System.currentTimeMillis(), uniqueId
            );

            if (billShare.isFailed()) {
                throw new KnownException(billShare.getMessage());
            }
            billShares.add(billShare.getValue());
        }

        // TODO : Move the below add in transaction
        BillEntity billEntity = billRepository.save(billResult.getValue());
        billShares.forEach(billShareEntity -> billShareEntity.setBillId(billEntity.getId()));
        List<BillShareEntity> billShareModel = billShareRepository.saveAll(billShares);

        return new BillSaveDetailDto(billEntity, billShareModel);
    }


    @Override
    public BillAllDto getAllBills(Integer groupId) throws KnownException {
        BillAllDto billAllDto = new BillAllDto();

        List<BillEntity> allBillEntity = billRepository.findAllByGroupId(groupId, Sort.by(Sort.Direction.DESC, "dateCreated"));
        List<UserDto> users = userProfileService.getAllUsers(groupId);

        List<Integer> uniqueIds = users.stream().map(UserDto::getUniqueId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Map<Integer, UserProfileEntity> userIdToUserMap = userProfileService.getProfiles(uniqueIds)
                .stream().collect(Collectors.toMap(UserProfileEntity::getUniqueId, profileEntity -> profileEntity));

        Map<Integer, UserDto> usersEntityMap = new HashMap<>();
        users.forEach(usersEntity -> {
            String profileUrl = null;
            if (usersEntity.getUniqueId() != null) {
                profileUrl = userIdToUserMap.get(usersEntity.getUniqueId()).getPhotoUrl();
            }
            usersEntityMap.put(usersEntity.getId(), new UserDto(usersEntity.getId(), usersEntity.getGroupId(),
                    usersEntity.getName(), profileUrl, usersEntity.getDateCreated(), usersEntity.getUniqueId())
            );
        });


        List<BillDto> allBill = new ArrayList<>();
        allBillEntity.forEach(billEntity -> {

            BillDto bill = new BillDto(
                    billEntity.getId(),
                    billEntity.getGroupId(),
                    billEntity.getName(),
                    billEntity.getTotalAmount(),
                    billEntity.getDateCreated(),
                    billEntity.getUniqueId(),
                    null
            );

            List<BillShareEntity> billShares = billShareRepository.findAllByBillId(billEntity.getId(), Sort.by(Sort.Direction.DESC, "dateCreated"));

            List<BillShareDetailDto> billSharesEntity = billShares.stream().map(billShareEntity -> new BillShareDetailDto(
                    billShareEntity.getId(),
                    billShareEntity.getBillId(),
                    billShareEntity.getUserId(),
                    null,
                    billShareEntity.getSpent(),
                    billShareEntity.getShare(),
                    billShareEntity.getDateCreated()
            )).collect(Collectors.toList());
            for (int i = 0; i < billSharesEntity.size(); i++) {
                billSharesEntity.get(i).setUserId(billShares.get(i).getUserId());
                billSharesEntity.get(i).setUser(usersEntityMap.get(billShares.get(i).getUserId()));
            }

            billSharesEntity = billSharesEntity.stream()
                    .sorted(Comparator.comparing(billShareEntityDto -> billShareEntityDto.getUser().getDateCreated()))
                    .collect(Collectors.toList());

            bill.setBillShares(billSharesEntity);

            allBill.add(bill);

        });
        billAllDto.setData(allBill);

        return billAllDto;
    }

    @Override
    public BillDto deleteBills(BillDto billDto, Integer uniqueId) throws KnownException {
        BillEntity billEntity = billRepository.findOneById(billDto.getId());

        if(!Objects.equals(uniqueId, billEntity.getUniqueId()))
            throw new KnownException("Unauthorized Exception");

        billDto.getBillShares().forEach(billShareEntityDto -> {
            billShareRepository.deleteById(billShareEntityDto.getId());
        });

        billRepository.deleteById(billDto.getId());

        return billDto;
    }
}

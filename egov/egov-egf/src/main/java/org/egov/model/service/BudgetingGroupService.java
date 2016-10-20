package org.egov.model.service;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.lucene.util.CollectionUtil;
import org.egov.commons.CChartOfAccounts;
import org.egov.infra.admin.master.entity.AppConfigValues;
import org.egov.infra.admin.master.service.AppConfigValueService;
import org.egov.model.budget.BudgetGroup;
import org.egov.model.repository.BudgetingGroupRepository;
import org.egov.utils.Constants;
import org.egov.utils.FinancialConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

@Service
@Transactional(readOnly = true)
public class BudgetingGroupService {

    private final BudgetingGroupRepository budgetGroupRepository;

    @Autowired
    private AppConfigValueService appConfigValueService;

    @Autowired
    @Qualifier("parentMessageSource")
    private MessageSource messageSource;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public BudgetingGroupService(final BudgetingGroupRepository budgetGroupRepository) {
        this.budgetGroupRepository = budgetGroupRepository;
    }

    @Transactional
    public BudgetGroup create(final BudgetGroup budgetGroup) {
        return budgetGroupRepository.save(budgetGroup);
    }

    @Transactional
    public BudgetGroup update(final BudgetGroup budgetGroup) {
        return budgetGroupRepository.save(budgetGroup);
    }

    public List<BudgetGroup> findAll() {
        return budgetGroupRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public BudgetGroup findOne(final Long id) {
        return budgetGroupRepository.findOne(id);
    }

    public int getMajorCodeLength() {
        final List<AppConfigValues> appList = appConfigValueService.getConfigValuesByModuleAndKey(Constants.EGF,
                FinancialConstants.APPCONFIG_COA_MAJORCODE_LENGTH);
        return Integer.valueOf(appList.get(0).getValue());
    }

    public List<CChartOfAccounts> getMajorCodeList() {
        return budgetGroupRepository.findCOAByLength(getMajorCodeLength());
    }

    public List<CChartOfAccounts> getMinCodeList() {
        final String range = appConfigValueService
                .getConfigValuesByModuleAndKey(Constants.EGF, FinancialConstants.APPCONFIG_BUDGETGROUP_RANGE).get(0)
                .getValue();
        Integer minorCodeLength = 0;

        if (range.equalsIgnoreCase("minor"))
            minorCodeLength = Integer.valueOf(appConfigValueService
                    .getConfigValuesByModuleAndKey(Constants.EGF, FinancialConstants.APPCONFIG_COA_MINORCODE_LENGTH)
                    .get(0).getValue());
        else
            minorCodeLength = Integer.valueOf(appConfigValueService
                    .getConfigValuesByModuleAndKey(Constants.EGF, FinancialConstants.APPCONFIG_COA_DETAILCODE_LENGTH)
                    .get(0).getValue());
        return budgetGroupRepository.findCOAByLength(minorCodeLength);
    }

    public String validateBudgetGroup(final BudgetGroup budgetGroup, final BindingResult errors) {
        String validationMessage = "";

        BudgetGroup bg = null;
        List<BudgetGroup> bgCode = null;
        if (budgetGroup.getMajorCode() != null && budgetGroup.getId() == null)
            bg = budgetGroupRepository.findByMajorCode_Id(budgetGroup.getMajorCode().getId());
        else if (budgetGroup.getMajorCode() != null && budgetGroup.getId() != null)
            bg = budgetGroupRepository.findByMajorCode_IdAndIdNotIn(budgetGroup.getMajorCode().getId(),
                    budgetGroup.getId());
        if (bg != null)
            validationMessage = messageSource.getMessage("budgetgroup.invalid.majorcode", new String[] { bg.getName() },
                    null);

        if (budgetGroup.getMinCode() != null && budgetGroup.getMaxCode() != null && budgetGroup.getId() == null)
            bgCode = budgetGroupRepository.findByMinCodeGlcodeLessThanEqualAndMaxCodeGlcodeGreaterThanEqual(
                    budgetGroup.getMaxCode().getGlcode(), budgetGroup.getMinCode().getGlcode());
        else if (budgetGroup.getMinCode() != null && budgetGroup.getMaxCode() != null && budgetGroup.getId() != null)
            bgCode = budgetGroupRepository.findByMinCodeGlcodeLessThanEqualAndMaxCodeGlcodeGreaterThanEqualAndIdNotIn(
                    budgetGroup.getMinCode().getGlcode(), budgetGroup.getMinCode().getGlcode(), budgetGroup.getId());
        else
            bgCode = Collections.emptyList();

        if (!bgCode.isEmpty())
            validationMessage = messageSource.getMessage("budgetgroup.invalid.maxmincode",
                    new String[] { bgCode.get(0).getName() }, null, Locale.ENGLISH);

        List<BudgetGroup> bgList = budgetGroup.getMajorCode() != null
                ? budgetGroupRepository.getBudgetGroupForMappedMajorCode(
                        budgetGroup.getMajorCode().getGlcode().length(), budgetGroup.getMajorCode().getGlcode())
                : Collections.emptyList();

        if (!bgList.isEmpty())
            validationMessage = messageSource.getMessage("budgetgroup.invalid.majorcode1",
                    new String[] { bgList.get(0).getName() }, null);

        bg = budgetGroup.getMaxCode() != null ? budgetGroupRepository.getBudgetGroupForMinorCodesMajorCode(
                budgetGroup.getMaxCode().getGlcode().substring(0, getMajorCodeLength())) : null;
        if (bg != null)
            validationMessage = messageSource.getMessage("budgetgroup.invalid.maxcode1", new String[] { bg.getName() },
                    null, Locale.ENGLISH);

        bg = budgetGroup.getMinCode() != null ? budgetGroupRepository.getBudgetGroupForMinorCodesMajorCode(
                budgetGroup.getMinCode().getGlcode().substring(0, getMajorCodeLength())) : null;
        if (bg != null)
            validationMessage = messageSource.getMessage("budgetgroup.invalid.mincode1", new String[] { bg.getName() },
                    null, Locale.ENGLISH);

        return validationMessage;
    }

    public List<BudgetGroup> search(final BudgetGroup budgetGroup) {
        if (budgetGroup.getName() != null) {
            return budgetGroupRepository.findBudgetGroupByNameLike(budgetGroup.getName());
        } else
            return budgetGroupRepository.findAll();
    }
}
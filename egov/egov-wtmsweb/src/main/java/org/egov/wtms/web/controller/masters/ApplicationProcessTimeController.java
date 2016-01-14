/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.wtms.web.controller.masters;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.egov.wtms.masters.entity.ApplicationProcessTime;
import org.egov.wtms.masters.service.ApplicationProcessTimeService;
import org.egov.wtms.masters.service.ApplicationTypeService;
import org.egov.wtms.masters.service.ConnectionCategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping(value = "/masters")
public class ApplicationProcessTimeController {
    private final ConnectionCategoryService connectionCategoryService;
    
    private final ApplicationTypeService applicationTypeService;
    
    private final ApplicationProcessTimeService applicationProcessTimeService;
    
    @Autowired
    public ApplicationProcessTimeController(final ApplicationTypeService applicationTypeService,final ConnectionCategoryService connectionCategoryService,
            final ApplicationProcessTimeService applicationProcessTimeService) {
        this.connectionCategoryService = connectionCategoryService;
        this.applicationTypeService = applicationTypeService;
        this.applicationProcessTimeService = applicationProcessTimeService;
    }
    
    @RequestMapping(value = "/applicationProcessTime", method = GET)
    public String viewForm(@ModelAttribute ApplicationProcessTime applicationProcessTime, final Model model) {
        applicationProcessTime = new ApplicationProcessTime();
        model.addAttribute("applicationProcessTime", applicationProcessTime);
        model.addAttribute("connectionCategories", connectionCategoryService.getAllActiveConnectionCategory());
        model.addAttribute("applicationTypes", applicationTypeService.findAll());
        return "application-process-time-master";
    }
    
    @RequestMapping(value = "/applicationProcessTime", method = RequestMethod.POST)
    public String addApplicationProcessTimeMasterData(@ModelAttribute ApplicationProcessTime applicationProcessTime,
            final RedirectAttributes redirectAttrs, final Model model, final BindingResult resultBinder) {
        if (resultBinder.hasErrors())
            return "application-process-time-master";
        applicationProcessTime.setActive(true);
        applicationProcessTimeService.createApplicationProcessTime(applicationProcessTime);
        redirectAttrs.addFlashAttribute("applicationProcessTime", applicationProcessTime);
        model.addAttribute("message", "Application Process Time Master Data created successfully");
        return "application-process-time-success";
    }
}

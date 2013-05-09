package org.gatein.ui.admin.importexport.beans;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.exoplatform.container.PortalContainer;
import org.gatein.management.api.ContentType;
import org.gatein.management.api.PathAddress;
import org.gatein.management.api.controller.ManagedRequest;
import org.gatein.management.api.controller.ManagedResponse;
import org.gatein.management.api.controller.ManagementController;
import org.gatein.management.api.operation.OperationNames;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

@ManagedBean(name = "importer")
@SessionScoped
public class ImportSiteBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Map<String,Object> importModes;

    static{
        importModes = new LinkedHashMap<String,Object>();
        importModes.put("Conserve", "conserve");
        importModes.put("Insert", "insert");
        importModes.put("Merge", "merge");
        importModes.put("Overwrite", "overwrite");
    }

    public Map<String,Object> getImportModes() {
        return importModes;
    }

    private String importMode = "merge";

    public String getImportMode() {
        return importMode;
    }

    public void setImportMode(String importMode) {
        this.importMode = importMode;
    }

    public void importSite(FileUploadEvent event) throws Exception {
        UploadedFile item = event.getUploadedFile();

        ManagementController controller = (ManagementController) PortalContainer.getComponent(ManagementController.class);

        Map<String, List<String>> attributes = new HashMap<String, List<String>>(1);
        attributes.put("importMode", Collections.singletonList(importMode));

        ManagedRequest request = ManagedRequest.Factory.create(OperationNames.IMPORT_RESOURCE,
                PathAddress.pathAddress("mop"), attributes, event.getUploadedFile().getInputStream(), ContentType.ZIP);

        ManagedResponse response = controller.execute(request);
        if (!response.getOutcome().isSuccess()) {
            throw new Exception(response.getOutcome().getFailureDescription());
        }

        System.err.println("GOT: '" + item.getName() + "'");
    }

}

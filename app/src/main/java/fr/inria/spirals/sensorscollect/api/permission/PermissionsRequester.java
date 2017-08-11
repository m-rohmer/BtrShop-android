package fr.inria.spirals.sensorscollect.api.permission;

import java.util.Set;

public interface PermissionsRequester {

    Set<String> getRequiredPermissions();

    void onRequiredPermissionChange(String permission, int state);

}

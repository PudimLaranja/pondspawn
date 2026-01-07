package com.origin.pondspawn.component.interfaces;

import org.ladysnake.cca.api.v3.component.Component;

import java.util.UUID;

public interface TongueComponent extends Component {
    void setTongueUuid(UUID uuid);
    UUID getTongueUuid();
}

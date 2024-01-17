package io.srk.pastecode;

import io.srk.pastecode.code.repository.CodeRepository;
import io.srk.pastecode.user.repository.UserRepository;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

public abstract class IntegrationTest extends TestcontainersTest {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected CodeRepository codeRepository;

    @MockBean
    protected RealmResource realmResource;
}

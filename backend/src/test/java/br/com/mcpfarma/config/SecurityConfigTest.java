
package br.com.mcpfarma.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveBloquearAcessoSemAutenticacao() throws Exception {
        mockMvc.perform(get("/api/v1/produtos"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void devePermitirAcessoAutenticado() throws Exception {
        mockMvc.perform(get("/api/v1/produtos"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void deveBloquearCatalogoPublico() throws Exception {
        mockMvc.perform(get("/api/v1/catalogo-publico/produtos"))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username = "operador", roles = "USER")
    void deveBloquearUsuarioSemPermissaoDeAdministrador()
            throws Exception {

        mockMvc.perform(get("/api/v1/produtos"))
                .andExpect(status().isForbidden());
    }

}

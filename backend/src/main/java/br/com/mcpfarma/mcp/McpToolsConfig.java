
package br.com.mcpfarma.mcp;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpToolsConfig {

    @Bean
    public ToolCallbackProvider produtoToolCallbackProvider(
            ProdutoMcpTools produtoMcpTools) {

        return MethodToolCallbackProvider.builder()
                .toolObjects(produtoMcpTools)
                .build();
    }
}

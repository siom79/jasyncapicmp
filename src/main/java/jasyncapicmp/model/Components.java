package jasyncapicmp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class Components implements Model {
    private Map<String, Schema> schemas = new HashMap<>();
    private Map<String, Server> servers = new HashMap<>();
    private Map<String, Channel> channels = new HashMap<>();
    private Map<String, Operation> operations = new HashMap<>();
    private Map<String, Message> messages = new HashMap<>();
    private Map<String, SecurityScheme> securitySchemes = new HashMap<>();
    private Map<String, ServerVariable> serverVariables = new HashMap<>();
    private Map<String, Parameter> parameters = new HashMap<>();
    private Map<String, CorrelationId> correlationIds = new HashMap<>();
    private Map<String, OperationReply> replies = new HashMap<>();
    private Map<String, OperationReplyAddress> replyAddresses = new HashMap<>();
    private Map<String, ExternalDocumentation> externalDocs = new HashMap<>();
    private Map<String, Tag> tags = new HashMap<>();
    private Map<String, OperationTrait> operationTraits = new HashMap<>();
    private Map<String, MessageTrait> messageTraits = new HashMap<>();
    private Map<String, ServerBinding>  serverBindings = new HashMap<>();
    private Map<String, ChannelBinding>  channelBindings = new HashMap<>();
    private Map<String, OperationBinding>  operationBindings = new HashMap<>();
    private Map<String, MessageBinding>  messageBindings = new HashMap<>();
}

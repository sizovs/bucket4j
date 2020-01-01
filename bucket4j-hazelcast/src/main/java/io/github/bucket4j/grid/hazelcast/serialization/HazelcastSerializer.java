package io.github.bucket4j.grid.hazelcast.serialization;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;
import com.hazelcast.nio.serialization.TypedStreamDeserializer;
import io.github.bucket4j.*;
import io.github.bucket4j.grid.CommandResult;
import io.github.bucket4j.grid.GridBucketState;
import io.github.bucket4j.grid.jcache.ExecuteProcessor;
import io.github.bucket4j.grid.jcache.InitStateAndExecuteProcessor;
import io.github.bucket4j.grid.jcache.InitStateProcessor;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.BucketState;
import io.github.bucket4j.EstimationProbe;
import io.github.bucket4j.grid.*;
import io.github.bucket4j.serialization.SerializationHandle;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class HazelcastSerializer<T> implements StreamSerializer<T>, TypedStreamDeserializer<T> {

    // serializers for persisted state
    public static HazelcastSerializer<Bandwidth> BANDWIDTH_SERIALIZER = new HazelcastSerializer<>(1, Bandwidth.class, Bandwidth.SERIALIZATION_HANDLE);
    public static HazelcastSerializer<BucketConfiguration> BUCKET_CONFIGURATION_SERIALIZER = new HazelcastSerializer<>(2, BucketConfiguration.class, BucketConfiguration.SERIALIZATION_HANDLE);
    public static HazelcastSerializer<BucketState> BUCKET_STATE_SERIALIZER = new HazelcastSerializer<>(3, BucketState.class, BucketState.SERIALIZATION_HANDLE);
    public static HazelcastSerializer<GridBucketState> GRID_BUCKET_STATE_SERIALIZER = new HazelcastSerializer<>(4, GridBucketState.class, GridBucketState.SERIALIZATION_HANDLE);

    // serializers for commands
    public static HazelcastSerializer<ReserveAndCalculateTimeToSleepCommand> RESERVE_AND_CALCULATE_TIME_TO_SLEEP_COMMAND_SERIALIZER = new HazelcastSerializer<>(5, ReserveAndCalculateTimeToSleepCommand.class, ReserveAndCalculateTimeToSleepCommand.SERIALIZATION_HANDLE);
    public static HazelcastSerializer<AddTokensCommand> ADD_TOKENS_COMMAND_SERIALIZER = new HazelcastSerializer<>(6, AddTokensCommand.class, AddTokensCommand.SERIALIZATION_HANDLE);
    public static HazelcastSerializer<ConsumeAsMuchAsPossibleCommand> CONSUME_AS_MUCH_AS_POSSIBLE_COMMAND_SERIALIZER = new HazelcastSerializer<>(7, ConsumeAsMuchAsPossibleCommand.class, ConsumeAsMuchAsPossibleCommand.SERIALIZATION_HANDLE);
    public static HazelcastSerializer<CreateSnapshotCommand> CREATE_SNAPSHOT_COMMAND_SERIALIZER = new HazelcastSerializer<>(8, CreateSnapshotCommand.class, CreateSnapshotCommand.SERIALIZATION_HANDLE);
    public static HazelcastSerializer<GetAvailableTokensCommand> GET_AVAILABLE_TOKENS_COMMAND_SERIALIZER = new HazelcastSerializer<>(9, GetAvailableTokensCommand.class, GetAvailableTokensCommand.SERIALIZATION_HANDLE);
    public static HazelcastSerializer<EstimateAbilityToConsumeCommand> ESTIMATE_ABILITY_TO_CONSUME_COMMAND_SERIALIZER = new HazelcastSerializer<>(10, EstimateAbilityToConsumeCommand.class, EstimateAbilityToConsumeCommand.SERIALIZATION_HANDLE);
    public static HazelcastSerializer<TryConsumeCommand> TRY_CONSUME_COMMAND_SERIALIZER = new HazelcastSerializer<>(11, TryConsumeCommand.class, TryConsumeCommand.SERIALIZATION_HANDLE);
    public static HazelcastSerializer<TryConsumeAndReturnRemainingTokensCommand> TRY_CONSUME_AND_RETURN_REMAINING_TOKENS_COMMAND_SERIALIZER = new HazelcastSerializer<>(12, TryConsumeAndReturnRemainingTokensCommand.class, TryConsumeAndReturnRemainingTokensCommand.SERIALIZATION_HANDLE);
    public static HazelcastSerializer<ReplaceConfigurationOrReturnPreviousCommand> REPLACE_CONFIGURATION_OR_RETURN_PREVIOUS_COMMAND_SERIALIZER = new HazelcastSerializer<>(13, ReplaceConfigurationOrReturnPreviousCommand.class, ReplaceConfigurationOrReturnPreviousCommand.SERIALIZATION_HANDLE);

    // serializers for command results
    public static HazelcastSerializer<CommandResult<?>> COMMAND_RESULT_SERIALIZER = new HazelcastSerializer<CommandResult<?>>(14, (Class) CommandResult.class, CommandResult.SERIALIZATION_HANDLE);
    public static HazelcastSerializer<ConsumptionProbe> CONSUMPTION_PROBE_SERIALIZER = new HazelcastSerializer<>(15, ConsumptionProbe.class, ConsumptionProbe.SERIALIZATION_HANDLE);
    public static HazelcastSerializer<EstimationProbe> ESTIMATION_PROBE_SERIALIZER = new HazelcastSerializer<>(16, EstimationProbe.class, EstimationProbe.SERIALIZATION_HANDLE);

    // serializers for JCache related classes
    public static HazelcastSerializer<ExecuteProcessor<?, ?>> EXECUTE_PROCESSOR_SERIALIZER = new HazelcastSerializer<>(17, (Class) ExecuteProcessor.class, ExecuteProcessor.SERIALIZATION_HANDLE);
    public static HazelcastSerializer<InitStateProcessor> INIT_STATE_PROCESSOR_SERIALIZER = new HazelcastSerializer<>(18, (Class) InitStateProcessor.class, InitStateProcessor.SERIALIZATION_HANDLE);
    public static HazelcastSerializer<InitStateAndExecuteProcessor<?, ?>> INIT_STATE_AND_EXECUTE_PROCESSOR_SERIALIZER = new HazelcastSerializer<>(19, (Class) InitStateAndExecuteProcessor.class, InitStateAndExecuteProcessor.SERIALIZATION_HANDLE);

    /**
     * Returns the list custom Hazelcast serializers for all classes from Bucket4j library which can be transferred over network.
     * Each serializer will have different typeId, and this id will not be changed in the feature releases.
     *
     * <p>
     *     <strong>Note:</strong> it would be better to leave an empty space in the Ids in order to handle the extension of Bucket4j library when new classes can be added to library.
     *     For example if you called {@code getAllSerializers(10000)} then it would be reasonable to avoid registering your custom types in the interval 10000-10100.
     * </p>
     *
     * @param typeIdBase a starting number from for typeId sequence
     *
     * @return
     */
    public static List<HazelcastSerializer<?>> getAllSerializers(final int typeIdBase) {
        return Arrays.asList(
                BANDWIDTH_SERIALIZER.withBaseTypeId(typeIdBase),
                BUCKET_CONFIGURATION_SERIALIZER.withBaseTypeId(typeIdBase),
                BUCKET_STATE_SERIALIZER.withBaseTypeId(typeIdBase),
                GRID_BUCKET_STATE_SERIALIZER.withBaseTypeId(typeIdBase),

                RESERVE_AND_CALCULATE_TIME_TO_SLEEP_COMMAND_SERIALIZER.withBaseTypeId(typeIdBase),
                ADD_TOKENS_COMMAND_SERIALIZER.withBaseTypeId(typeIdBase),
                CONSUME_AS_MUCH_AS_POSSIBLE_COMMAND_SERIALIZER.withBaseTypeId(typeIdBase),
                CREATE_SNAPSHOT_COMMAND_SERIALIZER.withBaseTypeId(typeIdBase),
                GET_AVAILABLE_TOKENS_COMMAND_SERIALIZER.withBaseTypeId(typeIdBase),
                ESTIMATE_ABILITY_TO_CONSUME_COMMAND_SERIALIZER.withBaseTypeId(typeIdBase),
                TRY_CONSUME_COMMAND_SERIALIZER.withBaseTypeId(typeIdBase),
                TRY_CONSUME_AND_RETURN_REMAINING_TOKENS_COMMAND_SERIALIZER.withBaseTypeId(typeIdBase),
                REPLACE_CONFIGURATION_OR_RETURN_PREVIOUS_COMMAND_SERIALIZER.withBaseTypeId(typeIdBase),

                COMMAND_RESULT_SERIALIZER.withBaseTypeId(typeIdBase),
                ESTIMATION_PROBE_SERIALIZER.withBaseTypeId(typeIdBase),
                CONSUMPTION_PROBE_SERIALIZER.withBaseTypeId(typeIdBase),

                EXECUTE_PROCESSOR_SERIALIZER.withBaseTypeId(typeIdBase),
                INIT_STATE_PROCESSOR_SERIALIZER.withBaseTypeId(typeIdBase),
                INIT_STATE_AND_EXECUTE_PROCESSOR_SERIALIZER.withBaseTypeId(typeIdBase)
        );
    }

    private static ReadWriteAdapter ADAPTER = new ReadWriteAdapter();

    private final int typeId;
    private final Class<T> serializableType;
    private final SerializationHandle<T> serializationHandle;

    public HazelcastSerializer(int typeId, Class<T> serializableType, SerializationHandle<T> serializationHandle) {
        this.typeId = typeId;
        this.serializableType = serializableType;
        this.serializationHandle = serializationHandle;
    }

    public HazelcastSerializer<T> withBaseTypeId(int baseTypeId) {
        return new HazelcastSerializer<>(typeId + baseTypeId, serializableType, serializationHandle);
    }

    public Class<T> getSerializableType() {
        return serializableType;
    }

    @Override
    public int getTypeId() {
        return typeId;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void write(ObjectDataOutput out, T serializable) throws IOException {
        serializationHandle.serialize(ADAPTER, out, serializable);
    }

    @Override
    public T read(ObjectDataInput in) throws IOException {
        return read0(in);
    }

    @Override
    public T read(ObjectDataInput in, Class aClass) throws IOException {
        return read0(in);
    }

    private T read0(ObjectDataInput in) throws IOException {
        return serializationHandle.deserialize(ADAPTER, in);
    }

}

/*
 *
 * Copyright 2015-2019 Vladimir Bukhtoyarov
 *
 *       Licensed under the Apache License, Version 2.0 (the "License");
 *       you may not use this file except in compliance with the License.
 *       You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */

package io.github.bucket4j.distributed.remote.commands;

import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.remote.CommandResult;
import io.github.bucket4j.distributed.remote.MutableBucketEntry;
import io.github.bucket4j.distributed.remote.RemoteBucketState;
import io.github.bucket4j.distributed.remote.RemoteCommand;
import io.github.bucket4j.distributed.serialization.DeserializationAdapter;
import io.github.bucket4j.distributed.serialization.SerializationAdapter;
import io.github.bucket4j.distributed.serialization.SerializationHandle;
import io.github.bucket4j.distributed.versioning.Version;
import io.github.bucket4j.distributed.versioning.Versions;
import io.github.bucket4j.util.ComparableByContent;

import java.io.IOException;

import static io.github.bucket4j.distributed.versioning.Versions.v_5_0_0;

public class GetConfigurationCommand implements RemoteCommand<BucketConfiguration>, ComparableByContent<GetConfigurationCommand> {

    @Override
    public CommandResult<BucketConfiguration> execute(MutableBucketEntry mutableEntry, long currentTimeNanos) {
        if (!mutableEntry.exists()) {
            return CommandResult.bucketNotFound();
        }

        RemoteBucketState state = mutableEntry.get();
        return CommandResult.success(state.getConfiguration(), BucketConfiguration.SERIALIZATION_HANDLE);
    }

    public static SerializationHandle<GetConfigurationCommand> SERIALIZATION_HANDLE = new SerializationHandle<GetConfigurationCommand>() {

        @Override
        public <S> GetConfigurationCommand deserialize(DeserializationAdapter<S> adapter, S input, Version backwardCompatibilityVersion) throws IOException {
            int formatNumber = adapter.readInt(input);
            Versions.check(formatNumber, v_5_0_0, v_5_0_0);

            return new GetConfigurationCommand();
        }

        @Override
        public <O> void serialize(SerializationAdapter<O> adapter, O output, GetConfigurationCommand command, Version backwardCompatibilityVersion) throws IOException {
            adapter.writeInt(output, v_5_0_0.getNumber());

            // do nothing
        }

        @Override
        public int getTypeId() {
            return 33;
        }

        @Override
        public Class<GetConfigurationCommand> getSerializedType() {
            return GetConfigurationCommand.class;
        }

    };

    @Override
    public SerializationHandle getSerializationHandle() {
        return SERIALIZATION_HANDLE;
    }

    @Override
    public boolean equalsByContent(GetConfigurationCommand other) {
        return true;
    }

    @Override
    public boolean isImmediateSyncRequired(long unsynchronizedTokens, long nanosSinceLastSync) {
        return false;
    }

    @Override
    public long estimateTokensToConsume() {
        return 0;
    }

    @Override
    public long getConsumedTokens(BucketConfiguration result) {
        return 0;
    }


}

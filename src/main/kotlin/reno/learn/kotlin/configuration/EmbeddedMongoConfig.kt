package reno.learn.kotlin.configuration

import de.flapdoodle.embed.mongo.MongodExecutable
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.MongodConfig
import de.flapdoodle.embed.mongo.config.Net
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network
import org.springframework.context.annotation.Configuration

@Configuration
class EmbeddedMongoConfig {

    // TODO("This must go to the test")

    init {
        val mongodbExecutable: MongodExecutable
        val ip = "localhost"
        val port = 27017
        val mongodbConfig = MongodConfig
            .builder()
            .version(Version.V6_0_2)
            .net(Net(ip, port, Network.localhostIsIPv6()))
            .build()
        val starter = MongodStarter.getDefaultInstance()
        mongodbExecutable = starter.prepare(mongodbConfig)
        mongodbExecutable.start()
    }
}

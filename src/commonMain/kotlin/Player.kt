import com.soywiz.korev.Key
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.addTo
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs

fun Container.player() = Player().addTo(this)

class Player : Container() {

    init {

    }
}
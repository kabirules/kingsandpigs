import com.soywiz.klock.*
import com.soywiz.kmem.clamp
import com.soywiz.korev.Key
import com.soywiz.korge.*
import com.soywiz.korge.tiled.readTiledMap
import com.soywiz.korge.tiled.tiledMapView
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*
import kotlin.math.pow

suspend fun main() = Korge(width = 640, height = 480, bgcolor = Colors["#2b2b2b"]) {
	var idleLeftSpriteMap = resourcesVfs["Idle (78x58).png"].readBitmap().flipX()
	var idleRightSpriteMap = resourcesVfs["Idle (78x58).png"].readBitmap()
	var runLeftSpriteMap = resourcesVfs["Run (78x58).png"].readBitmap().flipX()
	var runRightSpriteMap = resourcesVfs["Run (78x58).png"].readBitmap()
	val idleLeftAnimation = SpriteAnimation(
		spriteMap = idleLeftSpriteMap,
		spriteWidth = 78,
		spriteHeight = 58,
		columns = 11
	)
	val idleRightAnimation = SpriteAnimation(
		spriteMap = idleRightSpriteMap,
		spriteWidth = 78,
		spriteHeight = 58,
		columns = 11
	)
	val runLeftAnimation = SpriteAnimation(
		spriteMap = runLeftSpriteMap,
		spriteWidth = 78,
		spriteHeight = 58,
		columns = 8
	)
	val runRightAnimation = SpriteAnimation(
		spriteMap = runRightSpriteMap,
		spriteWidth = 78,
		spriteHeight = 58,
		columns = 8
	)
	val sprite = sprite() {
		scale(1)
		position(256, 256)
	}
	var lookingRight = true
	sprite.playAnimationLooped(idleRightAnimation, spriteDisplayTime = 100.milliseconds)
	val input = views.input
	sprite.addUpdater(fun Sprite.(dt: TimeSpan) {
		val scale = dt / 16.66666.milliseconds
		if (input.keys[Key.LEFT]) {
			sprite.x-- * scale
			sprite.playAnimationLooped(runLeftAnimation, spriteDisplayTime = 100.milliseconds)
			lookingRight = false
		} else if (input.keys[Key.RIGHT]) {
			sprite.x++ * scale
			sprite.playAnimationLooped(runRightAnimation, spriteDisplayTime = 100.milliseconds)
			lookingRight = true
		} else {
			if (lookingRight) {
				sprite.playAnimationLooped(idleRightAnimation, spriteDisplayTime = 50.milliseconds)
			} else {
				sprite.playAnimationLooped(idleLeftAnimation, spriteDisplayTime = 50.milliseconds)
			}
		}
	})
	// TILESET
	val tiledMap = resourcesVfs["sample.tmx"].readTiledMap()
	fixedSizeContainer(256, 256, clip = true) {
		position(128, 128)
		val camera = camera {
			tiledMapView(tiledMap) {
			}
		}
		var dx = 0.0
		var dy = 0.0
		addUpdater {
			//val scale = 1.0 / (it / 16.666666.hrMilliseconds)
			val scale = if (it == 0.milliseconds) 0.0 else (it / 16.666666.milliseconds)
			if (views.input.keys[Key.RIGHT]) dx -= 1.0
			if (views.input.keys[Key.LEFT]) dx += 1.0
			if (views.input.keys[Key.UP]) dy += 1.0
			if (views.input.keys[Key.DOWN]) dy -= 1.0
			dx = dx.clamp(-10.0, +10.0)
			dy = dy.clamp(-10.0, +10.0)
			camera.x += dx * scale
			camera.y += dy * scale
			dx *= 0.9.pow(scale)
			dy *= 0.9.pow(scale)
		}
	}
}




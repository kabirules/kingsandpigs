import com.soywiz.klock.*
import com.soywiz.korev.Key
import com.soywiz.korge.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*

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
}



import com.soywiz.klock.*
import com.soywiz.kmem.clamp
import com.soywiz.korev.Key
import com.soywiz.korge.*
import com.soywiz.korge.box2d.registerBodyWithFixture
import com.soywiz.korge.tiled.readTiledMap
import com.soywiz.korge.tiled.tiledMapView
import com.soywiz.korge.view.*
import com.soywiz.korge.view.ktree.viewTreeToKTree
import com.soywiz.korim.color.Colors
import com.soywiz.korim.font.readBitmapFont
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*
import org.jbox2d.dynamics.BodyType
import kotlin.math.pow

suspend fun main() = Korge(width = 640, height = 480, bgcolor = Colors["#2b2b2b"]) {
	val floor = resourcesVfs["floor.png"].readBitmap()
	image(floor).position(250,300).registerBodyWithFixture(type=BodyType.KINEMATIC)

	var idleLeftSpriteMap = resourcesVfs["Idle (78x58).png"].readBitmap().flipX()
	var idleRightSpriteMap = resourcesVfs["Idle (78x58).png"].readBitmap()
	var runLeftSpriteMap = resourcesVfs["Run (78x58).png"].readBitmap().flipX()
	var runRightSpriteMap = resourcesVfs["Run (78x58).png"].readBitmap()
	var attackLeftSpriteMap = resourcesVfs["Attack (78x58).png"].readBitmap().flipX()
	var attackRightSpriteMap = resourcesVfs["Attack (78x58).png"].readBitmap()

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
	val attackLeftAnimation = SpriteAnimation(
		spriteMap = attackLeftSpriteMap,
		spriteWidth = 78,
		spriteHeight = 58,
		columns = 3
	)
	val attackRightAnimation = SpriteAnimation(
		spriteMap = attackRightSpriteMap,
		spriteWidth = 78,
		spriteHeight = 58,
		columns = 3
	)
	val sprite = sprite() {
		scale(1)
		position(256, 256)
	}
	sprite.registerBodyWithFixture(type = BodyType.DYNAMIC)
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
		if (input.keys[Key.SPACE]) {
			if (lookingRight) {
				sprite.playAnimation(
					times = 1,
					attackRightAnimation,
					spriteDisplayTime = 100.milliseconds,
					reversed = true
				)
			} else {
				sprite.playAnimation(
					times = 1,
					attackLeftAnimation,
					spriteDisplayTime = 100.milliseconds,
					reversed = true
				)
			}
		}
	})
}




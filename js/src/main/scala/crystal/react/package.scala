package crystal

import crystal.react.implicits._
import cats.effect.Async
import cats.effect.ConcurrentEffect
import cats.effect.ContextShift
import japgolly.scalajs.react._
import io.chrisdavenport.log4cats.Logger

package object react {

  type SetState[F[_], A] = A => F[Unit]
  type ModState[F[_], A] = (A => A) => F[Unit]

  implicit class StreamOps[F[_], A](private val s: fs2.Stream[F, A]) {
    def render(implicit
      ce:     ConcurrentEffect[F],
      logger: Logger[F],
      reuse:  Reusability[A]
    ): StreamRenderer.Component[A] =
      StreamRenderer.build(s)
  }
}

package react {
  class FromStateViewF[F[_]]() {
    def apply[S](
      $              : BackendScope[_, S]
    )(implicit async: Async[F], cs: ContextShift[F]): ViewF[F, S] =
      ViewF($.state.runNow(), $.modStateIn[F])
  }
}

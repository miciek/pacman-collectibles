package com.michalplachta.pacman.collectibles.http
import com.michalplachta.pacman.collectibles.data.Position

final case class CreateCollectibles(id: Int, positions: Set[Position])

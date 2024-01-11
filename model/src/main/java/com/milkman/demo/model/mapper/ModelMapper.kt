package com.milkman.demo.augusto.model.mapper

import com.milkman.demo.model.BeerCacheModel
import com.milkman.demo.model.BeerModel

object ModelMapper {

    fun beerToCacheBeerMapper(model : BeerModel) : BeerCacheModel {
       return  BeerCacheModel(
            id = model.id,
            name = model.name,
            tagline = model.tagline,
            description = model.description,
            imageUrl = model.image_url
        )
    }

    fun cacheBeerToBeerMapper(model : BeerCacheModel) : BeerModel {
        return  BeerModel(
            id = model.id,
            name = model.name,
            tagline = model.tagline,
            description = model.description,
            image_url = model.imageUrl
        )
    }
}
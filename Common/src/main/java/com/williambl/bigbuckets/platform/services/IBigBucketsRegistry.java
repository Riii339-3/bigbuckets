package com.williambl.bigbuckets.platform.services;

import com.williambl.bigbuckets.BigBucketItem;
import com.williambl.bigbuckets.recipe.BigBucketIncreaseCapacityRecipe;
import com.williambl.bigbuckets.recipe.BigBucketRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

import java.util.function.Supplier;

public interface IBigBucketsRegistry {
    Supplier<BigBucketItem> bigBucketItem();
    Supplier<SimpleCraftingRecipeSerializer<BigBucketRecipe>> bigBucketRecipeSerializer();
    Supplier<SimpleCraftingRecipeSerializer<BigBucketIncreaseCapacityRecipe>> bigBucketIncreaseCapacityRecipeSerializer();
}

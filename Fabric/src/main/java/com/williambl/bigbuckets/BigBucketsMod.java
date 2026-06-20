package com.williambl.bigbuckets;

import com.williambl.bigbuckets.recipe.BigBucketIncreaseCapacityRecipe;
import com.williambl.bigbuckets.recipe.BigBucketRecipe;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

import static com.williambl.bigbuckets.BigBucketsCommon.id;

public class BigBucketsMod implements ModInitializer {

    public static FabricBigBucketItem BIG_BUCKET_ITEM = Registry.register(BuiltInRegistries.ITEM, id("bigbuckets:big_bucket"), new FabricBigBucketItem(new Item.Properties().stacksTo(1)));
    public static SimpleCraftingRecipeSerializer<BigBucketRecipe> BIG_BUCKET_RECIPE_SERIALIZER = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id("crafting_special_big_bucket"), new SimpleCraftingRecipeSerializer<>(BigBucketRecipe::new));
    public static SimpleCraftingRecipeSerializer<BigBucketIncreaseCapacityRecipe> BIG_BUCKET_INCREASE_CAPACITY_RECIPE_SERIALIZER = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id("crafting_special_big_bucket_increase_capacity"), new SimpleCraftingRecipeSerializer<>(BigBucketIncreaseCapacityRecipe::new));

    @Override
    public void onInitialize() {
        BigBucketsCommon.init();

        //noinspection UnstableApiUsage
        FluidStorage.ITEM.registerForItems((i, c) -> new FabricBigBucketItem.BigBucketStorage(c), BIG_BUCKET_ITEM);
    }
}

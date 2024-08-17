<script>
import { Cropper } from "vue-advanced-cropper";
import "vue-advanced-cropper/dist/style.css";
import NavigationForCropper from '@/components/NavigationForCropper.vue'

export default {
  components: {
    Cropper,
    NavigationForCropper
  },
  props: {
    file: {
      type: File,
      required: true,
    },
    aspectRatio: {
      type: Number,
      default: 1,
    },
  },
  data() {
    return {
      zoom: 0,
      imageSrc: '',
    };
  },
  watch: {
    file: {
      immediate: true,
      handler(newFile) {
        if (newFile) {
          this.loadImage(newFile);
        }
      },
    },
  },
  methods: {
    loadImage(file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        this.imageSrc = e.target.result;
      };
      reader.readAsDataURL(file);
    },
    getCroppedImage() {
      return new Promise((resolve, reject) => {
        this.$refs.cropper.getCroppedCanvas().toBlob((blob) => {
          if (blob) {
            // Тут можна дати ім'я файлу, наприклад "cropped-image.png"
            const file = new File([blob], "cropped-image.png", { type: "image/png" });
            resolve(file);
          } else {
            reject(new Error("Failed to crop image"));
          }
        }, "image/png");
      });
    },
    defaultSize({ imageSize }) {
      return {
        width: Math.min(imageSize.height, imageSize.width),
        height: Math.min(imageSize.height, imageSize.width),
      };
    },
    stencilSize({ boundaries }) {
      return {
        width: Math.min(boundaries.height, boundaries.width) - 48,
        height: Math.min(boundaries.height, boundaries.width) - 48,
      };
    },
    onChange(result) {
      const cropper = this.$refs.cropper;
      if (cropper) {
        const { coordinates, imageSize } = cropper;
        if (
          imageSize.width / imageSize.height >
          coordinates.width / coordinates.height
        ) {
          // Determine the position of slider bullet
          // It's 0 if the stencil has the maximum size and it's 1 if the has the minimum size
          this.zoom =
            (cropper.imageSize.height - cropper.coordinates.height) /
            (cropper.imageSize.height - cropper.sizeRestrictions.minHeight);
        } else {
          this.zoom =
            (cropper.imageSize.width - cropper.coordinates.width) /
            (cropper.imageSize.width - cropper.sizeRestrictions.minWidth);
        }
      }
    },
    onZoom(value) {
      const cropper = this.$refs.cropper;
      if (cropper) {
        if (cropper.imageSize.height < cropper.imageSize.width) {
          const minHeight = cropper.sizeRestrictions.minHeight;
          const imageHeight = cropper.imageSize.height;
          // Determine the current absolute zoom and the new absolute zoom
          // to calculate the sought relative zoom value
          cropper.zoom(
            (imageHeight - this.zoom * (imageHeight - minHeight)) /
            (imageHeight - value * (imageHeight - minHeight))
          );
        } else {
          const minWidth = cropper.sizeRestrictions.minWidth;
          const imageWidth = cropper.imageSize.width;
          cropper.zoom(
            (imageWidth - this.zoom * (imageWidth - minWidth)) /
            (imageWidth - value * (imageWidth - minWidth))
          );
        }
      }
    },
  },
};
</script>

<template>
  <div>
    <cropper
      ref="cropper"
      class="twitter-cropper"
      background-class="twitter-cropper__background"
      foreground-class="twitter-cropper__foreground"
      image-restriction="stencil"
      :stencil-size="stencilSize"
      :stencil-props="{
        lines: {},
        handlers: {},
        movable: false,
        scalable: false,
        aspectRatio: this.aspectRatio,
        previewClass: 'twitter-cropper__stencil',
      }"
      :transitions="false"
      :canvas="false"
      :debounce="false"
      :default-size="defaultSize"
      :min-width="150"
      :min-height="150"
      :src="imageSrc"
      @change="onChange"
    />
    <navigation-for-cropper :zoom="zoom" @change="onZoom" />
  </div>
</template>

<style scoped lang="scss">
.twitter-cropper {
  height: 521px;
  &__background {
    background-color: #edf2f4;
  }
  &__foreground {
    background-color: #edf2f4;
  }
  &__stencil {
    border: solid 5px rgb(29, 161, 242);
  }
}
</style>
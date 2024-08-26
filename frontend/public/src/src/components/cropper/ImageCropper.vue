<script>
import { Cropper } from "vue-advanced-cropper";
import "vue-advanced-cropper/dist/style.css";
import NavigationForCropper from '@/components/cropper/NavigationForCropper.vue'

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
      result: null
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
        const cropper = this.$refs.cropper;

        if (cropper) {
          // Отримання обрізаного зображення
          const {canvas} = cropper.getResult();

          if (canvas) {
            canvas.toBlob(blob => {
              if (blob) {
                const file = new File([blob], "cropped-image.png", { type: "image/png" });
                resolve(file);
              } else {
                reject(new Error("Failed to crop image"));
              }
            }, "image/png");
          } else {
            reject(new Error("Failed to create cropped canvas"));
          }
        } else {
          reject(new Error("Cropper reference not found"));
        }
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
    <Cropper
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
        scalable: true,
        aspectRatio: this.aspectRatio,
        previewClass: 'twitter-cropper__stencil',
      }"
        :transitions="false"
        :canvas="true"
        :debounce="false"
        :default-size="defaultSize"
        :min-width="150"
        :min-height="150"
        :src="imageSrc"
        @change="onChange"
    />
    <navigation-for-cropper :zoom="zoom" @change="onZoom"/>
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

<!--<template>-->
<!--  <div class="upload-example">-->
<!--    <Cropper ref="cropper" class="upload-example-cropper" :src="image"/>-->
<!--    <div class="button-wrapper">-->
<!--        <span class="button" @click="$refs.file.click()">-->
<!--          <input type="file" ref="file" @change="uploadImage($event)" accept="image/*">-->
<!--          Upload image-->
<!--        </span>-->
<!--      <span class="button" @click="cropImage">Crop image</span>-->
<!--    </div>-->
<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--import { Cropper } from "vue-advanced-cropper";-->

<!--export default {-->
<!--  data() {-->
<!--    return {-->
<!--      fileSize: null,-->
<!--      image: null-->
<!--    };-->
<!--  },-->
<!--  methods: {-->
<!--    cropImage(blob) {-->
<!--      const {canvas} = this.$refs.cropper.getResult();-->
<!--      canvas.toBlob(result => {-->
<!--        console.log(result);-->
<!--      }, 'image/jpeg')-->

<!--      //console.log(blob)-->
<!--    },-->
<!--    uploadImage(event) {-->
<!--      // Reference to the DOM input element-->
<!--      var input = event.target;-->
<!--      // Ensure that you have a file before attempting to read it-->
<!--      if (input.files && input.files[0]) {-->
<!--        // create a new FileReader to read this image and convert to base64 format-->
<!--        var reader = new FileReader();-->
<!--        // Define a callback function to run, when FileReader finishes its job-->
<!--        reader.onload = e => {-->
<!--          // Note: arrow function used here, so that "this.imageData" refers to the imageData of Vue component-->
<!--          // Read image as base64 and set to imageData-->
<!--          this.image = e.target.result;-->
<!--        };-->
<!--        // Start the reader job - read file as a data url (base64 format)-->
<!--        reader.readAsDataURL(input.files[0]);-->
<!--        this.fileSize = input.files[0].size;-->
<!--      }-->
<!--    }-->
<!--  },-->
<!--  components: {-->
<!--    Cropper-->
<!--  }-->
<!--};-->
<!--</script>-->

<!--<style>-->
<!--.upload-example-cropper {-->
<!--  border: solid 1px #EEE;-->
<!--  min-height: 300px;-->
<!--  width: 100%;-->
<!--}-->

<!--.button-wrapper {-->
<!--  display: flex;-->
<!--  justify-content: center;-->
<!--  margin-top: 17px;-->
<!--}-->

<!--.button {-->
<!--  color: white;-->
<!--  font-size: 16px;-->
<!--  padding: 10px 20px;-->
<!--  background: #3fb37f;-->
<!--  cursor: pointer;-->
<!--  transition: background 0.5s;-->
<!--  font-family: Open Sans, Arial;-->
<!--  margin: 0 10px;-->
<!--}-->

<!--.button:hover {-->
<!--  background: #38d890;-->
<!--}-->

<!--.button input {-->
<!--  display: none;-->
<!--}-->
<!--</style>-->
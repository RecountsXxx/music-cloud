<template>
  <div class="container mt-5">
    <h2 class="mb-4">Upload Your Release</h2>

    <form>
      <div class="form-group d-block mb-4">
        <label for="releaseCover" class="align-content-center p-2">Cover</label>
        <input type="file" @change="onFileCoverChange" />
        <image-cropper ref="imageCropper" v-if="this.release.cover" :file="this.release.cover" />
      </div>


      <!-- Fields for album information -->
      <div class="form-group d-block mb-4">
        <label for="releaseTitle" class="align-content-center p-2">Title</label>
        <input
            type="text"
            id="releaseTitle"
            v-model="release.title"
            class="form-control"
            placeholder="Enter release title"
        />
      </div>

      <div class="form-group d-block mb-4">
        <label for="releaseDate" class="align-content-center p-2">Release Date</label>
        <input
            type="date"
            id="releaseDate"
            v-model="release.releaseDate"
            class="form-control"
        />
      </div>

      <div class="form-group d-block mb-4">
        <label for="releaseType" class="align-content-center p-2">Type</label>
        <select id="releaseType" v-model="release.type" class="form-control">
          <option value="SINGLE" selected>Single</option>
          <option value="EP">EP</option>
          <option value="ALBUM">Album</option>
        </select>
      </div>

      <div class="form-group d-block mb-4">
        <label for="description" class="align-content-center p-2">Description</label>
        <textarea
            id="description"
            v-model="release.description"
            class="form-control"
            rows="3"
            placeholder="Describe your release">
        </textarea>
      </div>

      <div class="form-group d-block mb-4">
        <label for="privacy" class="align-content-center p-2">Privacy</label>
        <div class="d-block">
          <div class="form-check form-check-inline">
            <input
                class="form-check-input"
                type="radio"
                id="privacyPublic"
                name="privacy"
                value="PUBLIC"
                v-model="release.privacy"
            />
            <label class="form-check-label" for="privacyPublic">Public</label>
          </div>
          <div class="form-check form-check-inline">
            <input
                class="form-check-input"
                type="radio"
                id="privacyPrivate"
                name="privacy"
                value="PRIVATE"
                v-model="release.privacy"
            />
            <label class="form-check-label" for="privacyPrivate">Private</label>
          </div>
        </div>
      </div>

      <div class="form-group d-block mb-4">
        <label for="releaseBuyLink" class="align-content-center p-2">Buy-link</label>
        <input
            type="text"
            id="releaseBuyLink"
            v-model="release.buyLink"
            class="form-control"
            placeholder="Enter buy-link"
        />
      </div>

      <div class="form-group d-block mb-5">
        <label for="releaseRecordLabel" class="align-content-center p-2">Record label</label>
        <input
            type="text"
            id="releaseRecordLabel"
            v-model="release.recordLabel"
            class="form-control"
            placeholder="Enter record label"
        />
      </div>

      <!-- Track list -->
      <h4 class="mb-3">Tracks:</h4>
      <draggable v-model="tracks" :item-key="item => item.id" @end="updateTrackPositions">
        <template #item="{ element : track }">
          <div class="track-item mb-4">
            <div class="input-group">
              <div class="align-content-center p-2">
                {{ track.position }}.
              </div>
              <input
                  type="text"
                  v-model="track.name"
                  class="form-control"
                  placeholder="Track name"
              />
              <div class="input-group-append">
                <div class="input-group-text">{{ uploadProgress[track.id] }}%</div>
              </div>
            </div>

            <multiselect
                v-model="track.genres"
                :options="genres"
                :multiple="true"
                label="name"
                track-by="id"
                placeholder="Select genres"
                class="form-control mt-3"
            >
            </multiselect>

            <vue3-tags-input :tags="track.tags" placeholder="Add tags" @on-tags-changed="updateTags(track, $event)" class="mt-3"/>
          </div>
        </template>
      </draggable>

      <!-- Button for adding songs -->
      <div class="mb-4">
        <button
            type="button"
            class="btn btn-primary"
            @click="openFileDialog">
          Add tracks
        </button>
        <input
            ref="fileInput"
            type="file"
            multiple
            @change="onFilesSelected"
            style="display: none;"
        />
      </div>

      <!-- Button to save the release -->
      <div class="form-group">
        <button type="button" class="btn btn-success" @click="uploadRelease">
          Save
        </button>
      </div>
    </form>
  </div>
</template>

<script>
import draggable from "vuedraggable"
import Vue3TagsInput from 'vue3-tags-input';
import Multiselect from 'vue-multiselect';
import ImageCropper from '@/components/cropper/ImageCropper.vue';
import {
  createRelease,
  requestFileId, songConvert,
  startUpload,
  uploadCover
} from "@/utils/query-system/query-actions/releaseActions.js";
import {toastInfo} from "@/utils/toast/toastNotification.js";
import { useSocketStore } from '@/stores/socketStore.js'
import { subscribeToCoverUpload } from '@/utils/socket/eventHandlers.js'

const socketStore = useSocketStore();

export default {
  computed: {},
  components: {
    ImageCropper,
    draggable,
    Vue3TagsInput,
    Multiselect,
  },
  data() {
    return {
      release: {
        cover: null,
        title: '',
        buyLink: '',
        recordLabel: '',
        releaseDate: '',
        type: 'SINGLE',
        description: '',
        privacy: 'PUBLIC'
      },
      tracks: [],
      genres: [],
      uploadProgress: {}
    };
  },
  async created() {
    // Getting genres from the backend when loading a component
    try {
      this.genres = [
        { id: '1', name: "Rock" },
        { id: '2', name: "Pop" },
        { id: '3', name: "Rap" },
      ] // It is assumed that the backend returns a list of objects with the id and name fields
    } catch (error) {
      console.error('Error fetching genres:', error);
    }
  },
  methods: {
    openFileDialog() {
      this.$refs.fileInput.click();
    },


    onFileCoverChange(event) {
      const file = event.target.files[0];
      if (file) {
        this.release.cover = file;
      }
    },


    async onFilesSelected(event) {
      const selectedFiles = Array.from(event.target.files);

      for (let file of selectedFiles) {
        try {
          const fileId = await requestFileId();
          const track = {
            file,
            name: file.name,
            id: fileId,
            position: this.tracks.length + 1,
            genres: [],
            tags: [],
          };
          this.tracks.push(track);
          this.uploadProgress = { ...this.uploadProgress, [track.id]: 0 };
          this.startUpload(track);
        } catch (error) {
          console.error('Failed to get fileId:', error);
        }
      }
    },


    updateTrackPositions() {
      this.tracks.forEach((track, index) => {
        track.position = index + 1;
      });
    },


    updateTags(track, newTags) {
      track.tags = newTags;
    },


    startUpload(track) {
      const onProgress = (percentage) => {
        this.uploadProgress = { ...this.uploadProgress, [track.id]: percentage };
      }

      startUpload(track, onProgress);
    },


    async uploadRelease() {
      // const releaseData = {
      //   title: this.release.title,
      //   releaseDate: this.release.releaseDate,
      //   type: this.release.type,
      //   description: this.release.description,
      //   privacy: this.release.privacy,
      //   buyLink: this.release.buyLink,
      //   recordLabel: this.release.recordLabel,
      //   tracks: this.tracks.map(track => ({
      //     fileId: track.id,
      //     name: track.name,
      //     position: track.position,
      //     genreIds: track.genres.map(genre => genre.id),
      //     tags: track.tags.map(tag => tag)
      //   }))
      // };

      //const collectionId = await createRelease(releaseData);

      //await this.uploadCover(collectionId);

      // const songId = '1';
      // const fileId = 'c339853a-f269-4ab0-8814-d9ded5c89e7f';
      //
      //
      // const message = await songConvert(songId, fileId);
      // if(message) {
      //   toastInfo(message);
      // }

      const collectionId = "1";

      await this.uploadCover(collectionId);
    },


    async uploadCover(collectionId) {
      const croppedImage = await this.$refs.imageCropper.getCroppedImage();

      const formData = new FormData();
      formData.append('cover', croppedImage);

      const message = await uploadCover(collectionId, formData);

      if(message) {
        toastInfo(message);
        subscribeToCoverUpload();
      }
    }
  }
};
</script>

<style lang="scss">
@import "@/assets/styles/upload/UploadPage";
</style>
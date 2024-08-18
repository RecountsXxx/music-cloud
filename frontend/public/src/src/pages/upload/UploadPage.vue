<template>
  <div class="container mt-5">
    <!-- Заголовок форми -->
    <h2 class="mb-4">Upload Your Release</h2>

    <form>
      <div class="form-group d-block mb-4">
        <label for="releaseCover" class="align-content-center p-2">Cover</label>
        <input type="file" @change="onFileChange" />
        <image-cropper ref="imageCropper" v-if="this.release.cover" :file="this.release.cover" />
      </div>


      <!-- Поля для інформації про альбом -->
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
        <div class="d-block mb-4">
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

      <!-- Трек-лист -->
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

            <vue3-tags-input :tags="track.tags" placeholder="Add tags" @tags-changed="updateTags(track, $event)" class="mt-3"/>
          </div>
        </template>
      </draggable>

      <!-- Кнопка для додавання ще пісень -->
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

      <!-- Кнопка для збереження альбому -->
      <div class="form-group">
        <button type="button" class="btn btn-success" @click="saveAlbum">
          Save
        </button>
      </div>
    </form>
  </div>
</template>

<script>
import { Upload } from 'tus-js-client';
import draggable from "vuedraggable"
import Vue3TagsInput from 'vue3-tags-input';
import Multiselect from 'vue-multiselect';
import {useAuthStore} from "../../stores/authStore";
import ImageCropper from '@/components/ImageCropper.vue'
import { PerformQuery } from '@/utils/query-system/performQuery.js'
import { QueryMethods } from '@/utils/query-system/queryMethods.js'
import { QueryContentTypes } from '@/utils/query-system/queryContentTypes.js'
import { QueryPaths } from '@/utils/query-system/queryPaths.js'

const authStore = useAuthStore();

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
    // Отримання жанрів із бекенду при завантаженні компонента
    try {
      this.genres = [
        { id: '1', name: "Rock" },
        { id: '2', name: "Pop" },
        { id: '3', name: "Rap" },
      ] // Передбачається, що бекенд повертає список об'єктів з полями id і name
    } catch (error) {
      console.error('Error fetching genres:', error);
    }
  },
  methods: {
    openFileDialog() {
      this.$refs.fileInput.click();
    },


    onFileChange(event) {
      const file = event.target.files[0];
      if (file) {
        this.release.cover = file;
      }
    },


    async onFilesSelected(event) {
      const selectedFiles = Array.from(event.target.files);

      for (let file of selectedFiles) {
        try {
          const fileId = await this.requestFileId();
          const track = {
            file,
            name: file.name,
            id: fileId,
            position: this.tracks.length + 1,
            tags: []
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
        console.log("track name: " + track.name + ";   position in release:" + track.position);
      });
    },


    updateTags(track, newTags) {
      track.tags = newTags;
    },


    async requestFileId() {
      const data = await PerformQuery(QueryMethods.POST, QueryPaths.requestFileId(), null, QueryContentTypes.applicationJson, authStore.getJWT);
      return data.fileId;
    },


    startUpload(track) {
      const upload = new Upload(track.file, {
        endpoint: `${QueryPaths.baseApi()}${QueryPaths.uploadChunkedFile()}`,
        retryDelays: [0, 3000, 5000, 10000, 20000],
        chunkSize: 1000000,
        metadata: {
          filename: track.name,
          filetype: track.file.type,
          fileId: track.id,
        },
        headers: {
          Authorization: `Bearer ${authStore.getJWT}`
        },
        onError: (error) => {
          console.error('Upload failed:', error);
        },
        onProgress: (bytesUploaded, bytesTotal) => {
          const percentage = ((bytesUploaded / bytesTotal) * 100).toFixed(2);
          console.log(this.uploadProgress, track.id, percentage);
          this.uploadProgress = { ...this.uploadProgress, [track.id]: percentage };
        },
        onSuccess: () => {
          console.log("The file was successfully uploaded.");
        },
      });

      upload.findPreviousUploads().then((previousUploads) => {
        if (previousUploads.length) {
          console.info("Resuming previous upload.");
          upload.resumeFromPreviousUpload(previousUploads[0]);
        } else {
          console.info("Starting upload.");
          upload.start();
        }
      });
    },

    async saveAlbum() {
      // Логіка для збереження релізу
      const croppedImageFile = await this.$refs.imageCropper.getCroppedImage();
    }
  }
};
</script>

<style lang="scss">
.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

h2, h4 {
  color: #333;
  margin-bottom: 15px;
}

form {
  display: flex;
  flex-direction: column;
}

.input-group {
  margin-bottom: 15px;
}

.input-group input,
.input-group .multiselect {
  padding: 10px;
  border-radius: 4px;
  border: 1px solid #ccc;
  flex: 1;
}

.input-group .multiselect {
  margin-left: 30px;
}

.input-group .input-group-text {
  padding: 10px 15px;
  background-color: #fff;
  border-left: none;
  border-radius: 0 4px 4px 0;
  color: #555;
}

.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  background-color: #007bff;
  color: #fff;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-primary {
  margin-top: 10px;
  margin-bottom: 20px;
}

.btn:hover {
  background-color: #0056b3;
}

.form-group {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.track-item {
  background-color: #fff;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.track-item .input-group {
  align-items: center;
}

.track-item .multiselect {
  min-width: 200px;
}

.input-group {
  margin-bottom: 15px;
}

.track-item {
  margin-bottom: 20px;
  padding: 15px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #fff;
  box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1);
}

.multiselect {
  --multiselect-primary: #007bff;
  --multiselect-placeholder: #6c757d;
  --multiselect-background: #fff;
  --multiselect-border: #ced4da;
  --multiselect-selected-text: #fff;
  --multiselect-selected-background: #007bff;
}

.multiselect__option--selected {
  background-color: var(--multiselect-selected-background) !important;
  color: var(--multiselect-selected-text) !important;
}
</style>
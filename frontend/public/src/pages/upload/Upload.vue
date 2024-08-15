<template>
  <div class="container mt-5">
    <!-- Заголовок форми -->
    <h2 class="mb-4">Upload Your Release</h2>

    <form>
      <!-- Поля для інформації про альбом -->
      <div class="form-group mb-3">
        <label for="releaseTitle">Title</label>
        <input
            type="text"
            id="releaseTitle"
            v-model="release.title"
            class="form-control"
            placeholder="Enter release title"
        />
      </div>

      <div class="form-group mb-3">
        <label for="releaseDate">Release Date</label>
        <input
            type="date"
            id="releaseDate"
            v-model="release.releaseDate"
            class="form-control"
        />
      </div>

      <div class="form-group mb-3">
        <label for="genre">Genre</label>
        <select id="genre" v-model="release.genre" class="form-control">
          <option value="None">None</option>
          <option value="Rock">Rock</option>
          <option value="Pop">Pop</option>
          <option value="Hip-Hop">Hip-Hop</option>
          <option value="Jazz">Jazz</option>
          <!-- Додаткові жанри можна додати тут -->
        </select>
      </div>

      <div class="form-group mb-4">
        <label for="description">Description</label>
        <textarea
            id="description"
            v-model="release.description"
            class="form-control"
            rows="3"
            placeholder="Describe your release"
        ></textarea>
      </div>

      <!-- Трек-лист -->
      <h4 class="mb-3">Tracks</h4>
      <draggable v-model="tracks" :item-key="item => item.id" @end="updateTrackPositions">
        <template #item="{ element, index }">
          <div class="mb-3">
            <div class="input-group">
              <div class="align-content-center p-2">
                {{element.position}}.
              </div>
              <input
                  type="text"
                  v-model="element.name"
                  class="form-control"
                  placeholder="Track name"
              />
              <div class="input-group-append">
                <div class="input-group-text">{{ uploadProgress[element.id] }}%</div>
              </div>
            </div>
          </div>
        </template>
      </draggable>

      <!-- Кнопка для додавання ще пісень -->
      <div class="mb-4">
        <button
            type="button"
            class="btn btn-primary"
            @click="openFileDialog"
        >
          Add more tracks
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
import axios from 'axios';
import draggable from "vuedraggable"
import {useAuthStore} from "../../store/authStore";

const authStore = useAuthStore();

export default {
  computed: {},
  components: {
    draggable,
  },
  data() {
    return {
      release: {
        title: '',
        releaseDate: '',
        genre: 'None',
        description: ''
      },
      tracks: [],
      uploadProgress: {}
    };
  },
  methods: {
    openFileDialog() {
      this.$refs.fileInput.click();
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
            position: this.tracks.length + 1
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


    async requestFileId() {
      const response = await axios.post(`http://localhost/api/java/protected/upload/request-file-id`, {}, {
        headers: {
          Authorization: `Bearer ${authStore.getJWT}`
        }
      });
      return response.data.fileId;
    },


    startUpload(track) {
      const upload = new Upload(track.file, {
        endpoint: `http://localhost/api/java/protected/upload/file`,
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

    saveAlbum() {
      // Логіка для збереження релізу
    }
  }
};
</script>

<style scoped lang="scss">

</style>
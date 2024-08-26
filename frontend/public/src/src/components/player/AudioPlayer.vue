<script>
import videojs from 'video.js';
import 'video.js/dist/video-js.css';
import '@videojs/http-streaming';
import {QueryPaths} from "@/utils/query-system/queryPaths.js";
import {useAuthStore} from "@/stores/authStore.js";

const authStore = useAuthStore();

export default {
  data() {
    return {
      player: null,
      playlistUrl: ''
    };
  },
  async mounted() {
    const songId = '1';
    const quality = 'HIGH';

    const token = `Bearer ${authStore.getJWT}`;

    this.playlistUrl = `${QueryPaths.baseApi()}${QueryPaths.streamAudio(songId, quality)}`;

    if (this.playlistUrl) {

      videojs.Vhs.xhr = function(options, callback) {
        options = videojs.obj.merge(options, {
          beforeSend: function(xhr) {
            xhr.setRequestHeader('Authorization', token);
          }
        });
        return videojs.xhr(options, callback);
      };

      this.player = videojs(this.$refs.audioPlayer, {
        debug: true,
        techOrder: ['html5'],
        sources: [{
          src: this.playlistUrl,
          type: 'application/x-mpegURL'
        }]
      }, function() {
        console.log('Player is ready');
      });
    }
  },
  beforeUnmount() {
    if (this.player) {
      this.player.dispose();
    }
  }
};
</script>

<template>
  <div>
    <video ref="audioPlayer" class="video-js vjs-default-skin" controls preload="auto" width="600"></video>
  </div>
</template>

<style lang="scss">

.vjs-default-skin {
  width: 100%;
  height: 50px;
}
</style>
<div class="modal fade" id="comment-modal" role="dialog">
    <div class="modal-dialog modal-lg" style="width: 50%;">
        <!-- Modal local loop selection-->
        <div class="modal-content">
            <div class="modal-body">
                <template  v-if="commentsList!= null">
                    <template v-if="commentsList.length==0">
                        <p style="text-align: left;">No Comments to show!!</p><hr> <br>
                    </template>

                   <template v-for="comment in commentsList">
                    <p style="text-align: left;">{{comment.comment}}</p>
                    <hr> <br>
                   </template>
                </template>
                <template v-else  >
                    <p style="text-align: left;">No Comments to show!!</p><hr> <br>
                </template>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close
                </button>
            </div>
        </div>

    </div>
</div>

<div class="modal fade" id="add-comment-modal" role="dialog">
    <div class="modal-dialog modal-lg" style="width: 50%;">
        <!-- Modal local loop selection-->
        <div class="modal-content">
            <div class="modal-body">
                <div class="form-group">
                    <label for="comment">Comment:</label>
                    <textarea v-model="comment" class="form-control" rows="5" id="comment"></textarea>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" @click="addCommentOnThisLink()">Save</button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
            </div>
        </div>

    </div>
</div>
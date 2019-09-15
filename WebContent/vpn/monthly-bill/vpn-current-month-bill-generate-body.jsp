<div id=monthly-bill-generate>
	<btcl-body title="Monthly Bill" subtitle="Generate">
        	<btcl-portlet>
				<btcl-info title="Status" :text="msg"></btcl-info>           		
           		<button type=button v-if="!isGenerated" :disabled='clicked' class="btn green-haze btn-block btn-lg"  @click="findBillOfTheCurrentMonth">
           			Generate Current Month Bill
           		</button>           		         		 
           	</btcl-portlet>
	</btcl-body>
</div>
<script src=../../assets/month-map-int-str.js></script>
<script src=../monthly-bill/vpn-monthly-bill-generate.js></script>
       


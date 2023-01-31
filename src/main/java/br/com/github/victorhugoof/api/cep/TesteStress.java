package br.com.github.victorhugoof.api.cep;

import static br.com.github.victorhugoof.api.cep.helper.CepUtils.*;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.concurrent.CyclicBarrier;

@Slf4j
public class TesteStress {

    public static void main(String[] args) throws Exception {
        var searchList = Arrays.asList(1, 1014911, 1044000, 1153000, 1219902, 1238000, 1310100, 1310920, 1317002, 1407100, 2031200, 2047050, 2089900, 2265002, 2430001, 2545000, 2611001, 2611002, 2673000, 2675031, 2710000, 2736100, 2739000, 2831001, 2845120, 2846200, 2912010, 2983055, 2987100, 2989095, 2991000, 3076000, 3102006, 3220100, 3295000, 3342000, 3374000, 3452200, 3527900, 3542030, 3612020, 4029200, 4065004, 4133100, 4186090, 4203002, 4235470, 4296000, 4365000, 4366001, 4533013, 4538132, 4543011, 4551000, 4552000, 4571180, 4583903, 4661200, 4707000, 4707900, 4715004, 4742000, 4745070, 4795904, 4795999, 4801000, 4826010, 4849333, 5005030, 5036120, 5110000, 5112000, 5112010, 5114000, 5136001, 5145200, 5154000, 5157030, 5160100, 5171000, 5201050, 5268160, 5276000, 5328020, 5339003, 5402600, 5402918, 5477000, 5512300, 5576100, 5676120, 5690000, 5724020, 5777001, 5805000, 5844090, 5850210, 6016050, 6020010, 6065110, 6122160, 6126000, 6132004, 6132380, 6210000, 6216240, 6233020, 6240160, 6253000, 6268000, 6293130, 6298004, 6311000, 6320060, 6341530, 6365210, 6380021, 6401070, 6417000, 6436000, 6454913, 6455000, 6460000, 6460030, 6473073, 6501005, 6501130, 6534030, 6544310, 6600010, 6600080, 6653000, 6653020, 6653040, 6657000, 6703000, 6709150, 6716190, 6717210, 6803272, 6810605, 6823475, 6826176, 6826186, 6835248, 6865793, 6882700, 6888700, 6901990, 7034911, 7050000, 7053171, 7144280, 7160010, 7160490, 7161465, 7162000, 7174000, 7190100, 7242300, 7243410, 7400555, 7600030, 7621180, 7627185, 7700060, 7700210, 7713090, 7714030, 7739050, 7776450, 7786515, 7790500, 7790740, 7791700, 7791803, 7801000, 7802040, 7802260, 7803010, 7850323, 7851010, 7990040, 8060320, 8062200, 8080570, 8115000, 8160495, 8220900, 8290220, 8310620, 8320390, 8391603, 8500020, 8550100, 8563100, 8570060, 8590045, 8598000, 8674001, 8674003, 8675390, 8683060, 8710000, 8710040, 8710500, 8779700, 9020180, 9090410, 9110001, 9120560, 9120610, 9170090, 9180001, 9210030, 9250000, 9320795, 9370000, 9412000, 9440400, 9443410, 9445640, 9530210, 9531190, 9572200, 9580000, 9612000, 9616000, 9619000, 9650000, 9660000, 9664000, 9670000, 9695000, 9750660, 9750670, 9761000, 9770000, 9770055, 9861630, 9890000, 9891001, 9892000, 9950000, 9951000, 9951550, 9972200, 11010000, 11013000, 11015000, 11015504, 11020000, 11025000, 11025200, 11030000, 11035000, 11040000, 11045000, 11045100, 11055000, 11060000, 11065000, 11070000, 11075000, 11080000, 11081000, 11082000, 11083000, 11085000, 11086000, 11087000, 11088000, 11089000, 11090000, 11095000, 11096000, 11250225, 11258698, 11310071, 11440350, 11538060, 11629568, 11679552, 11680971, 11701000, 11717245, 11730000, 11740991, 11750970, 11770000, 11800000, 11850000, 11850970, 11855000, 11900000, 11900971, 11910970, 11930000, 11940000, 11940971, 11950000, 11960000, 11990970, 12043265, 12080000, 12092070, 12093246, 12093660, 12120174, 12227470, 12230000, 12248460, 12283260, 12290020, 12294433, 12328501, 12400000, 12400900, 12401521, 12402031, 12403260, 12404023, 12412752, 12442040, 12445220, 12522010, 12605605, 12800970, 12913511, 12922750, 12942280, 12954779, 13012000, 13012100, 13025085, 13030405, 13050700, 13056014, 13069360, 13082015, 13087901, 13092902, 13104096, 13140088, 13163142, 13174300, 13178337, 13188013, 13205705, 13210650, 13212316, 13213166, 13214792, 13215010, 13220001, 13225780, 13230100, 13231190, 13232297, 13233322, 13250329, 13260970, 13285534, 13288500, 13295000, 13300055, 13301000, 13306318, 13320050, 13330160, 13343803, 13360000, 13383872, 13405362, 13422150, 13451432, 13473570, 13480202, 13495000, 13503538, 13510000, 13562700, 13580000, 13601113, 13614162, 13690000, 13760000, 13805079, 13844123, 13855050, 13870251, 13912690, 13960000, 13990970, 14021630, 14033169, 14051030, 14073806, 14160140, 14270000, 14400970, 14500971, 14540000, 14620970, 14801017, 14810840, 14815970, 14920000, 14948416, 14960000, 15015400, 15025160, 15041494, 15050500, 15052775, 15057500, 15064186, 15080480, 15092175, 15093612, 15150000, 15170000, 15210000, 15210970, 15280970, 15385000, 15420000, 15501125, 15506736, 15507244, 15604170, 15606172, 15610390, 15706232, 15706446, 15800031, 15803010, 15809040, 15813286, 15816030, 15840000, 15908001, 15996050, 15999079, 16010000, 16200010, 16200080, 16304598, 16305082, 16403130, 16434508, 16450000, 16600047, 16640009, 16700000, 16880000, 16901000, 16950000, 17018000, 17054230, 17054697, 17055050, 17056000, 17123054, 17123160, 17160000, 17205000, 17209656, 17213409, 17213450, 17220127, 17220424, 17230000, 17250000, 17270044, 17273022, 17280000, 17300000, 17340000, 17518001, 17521551, 17521575, 17830970, 18010080, 18048110, 18061445, 18072110, 18072874, 18087691, 18115120, 18117720, 18118064, 18130290, 18132852, 18132900, 18170000, 18200180, 18200350, 18250000, 18255970, 18270001, 18270200, 18300120, 18315970, 18400180, 18400450, 18460000, 18480990, 18500000, 18535000, 18540000, 18550000, 18602092, 18606840, 18655000, 18700190, 18732026, 18740000, 18900370, 18903266, 18910266, 18935086, 19013030, 19026140, 19033390, 19190000, 19210000, 19230970, 19273970, 19275000, 19280991, 19360000, 19400000, 19400970, 19470000, 19500000, 19575000, 19640000, 19645000, 19814000, 19820970, 19860000, 19880000, 19900001, 19912120, 19912321, 19920015, 19940000, 19960000, 19970000, 20011030, 20011901, 20060070, 20510060, 20511000, 20760005, 20771004, 21720110, 21920445, 22230001, 22410001, 22410003, 22640902, 22775027, 22775130, 22783119, 22790587, 22793081, 23045000, 23560881, 23575070, 23895225, 23954275, 23968970, 23970000, 24350010, 24720110, 24754270, 24813704, 24815520, 24851600, 24865310, 25230480, 25243150, 25520661, 25745420, 26061421, 26163375, 26285010, 26310000, 26330000, 27211000, 27338150, 27524010, 28118000, 28480000, 28630340, 28907500, 28993270, 29010003, 29010004, 29010060, 29010080, 29010100, 29010120, 29010250, 29010270, 29010280, 29010361, 29010901, 29015100, 29015320, 29015330, 29016260, 29020470, 29026470, 29027080, 29027215, 29027245, 29031800, 29040750, 29041230, 29042715, 29042755, 29042810, 29043215, 29045055, 29045140, 29045200, 29045230, 29045300, 29045402, 29045403, 29045430, 29045450, 29045453, 29045460, 29045480, 29045530, 29045560, 29045580, 29045660, 29045720, 29046050, 29047495, 29047550, 29048550, 29050280, 29050420, 29050670, 29050810, 29052110, 29052160, 29052210, 29052280, 29052290, 29052320, 29053360, 29055131, 29055260, 29055270, 29055310, 29055410, 29055460, 29055590, 29056243, 29056245, 29056255, 29056300, 29056930, 29057565, 29060040, 29060270, 29060290, 29060420, 29060500, 29060670, 29062558, 29065035, 29065270, 29065340, 29066370, 29070010, 29072340, 29075010, 29075015, 29075053, 29075080, 29075140, 29090060, 29090090, 29090100, 29090130, 29090200, 29090370, 29090410, 29090540, 29090900, 29090912, 29100010, 29100011, 29100020, 29100021, 29100155, 29100200, 29100201, 29100250, 29100290, 29100320, 29100400, 29100401, 29100540, 29100906, 29101345, 29101390, 29101410, 29101420, 29101430, 29101500, 29101605, 29101641, 29101700, 29101916, 29102020, 29102040, 29102065, 29102340, 29102844, 29102906, 29103091, 29103095, 29103610, 29104025, 29104453, 29105000, 29105580, 29106400, 29106640, 29107010, 29108014, 29108330, 29108480, 29108640, 29109630, 29110050, 29110110, 29110180, 29110286, 29111630, 29112043, 29112150, 29112210, 29114490, 29117120, 29117500, 29117640, 29117660, 29118033, 29118200, 29118710, 29119060, 29120005, 29120568, 29122036, 29122290, 29122300, 29122310, 29122355, 29122725, 29123001, 29124086, 29124214, 29125050, 29126705, 29127206, 29128542, 29129677, 29130715, 29131040, 29134479, 29135000, 29140000, 29140020, 29140050, 29140070, 29140170, 29140190, 29140261, 29140740, 29141560, 29141590, 29144080, 29145470, 29146012, 29146040, 29146070, 29146080, 29146140, 29146201, 29146300, 29146420, 29148100, 29148390, 29148460, 29148610, 29148618, 29148680, 29148900, 29149001, 29149400, 29149520, 29150270, 29150730, 29151410, 29151680, 29151815, 29153100, 29153450, 29153630, 29154200, 29154580, 29157405, 29160000, 29160030, 29160042, 29160173, 29160321, 29160752, 29160771, 29160790, 29161019, 29161060, 29161160, 29161376, 29161562, 29161700, 29161900, 29162010, 29162155, 29163165, 29163266, 29163274, 29163331, 29163405, 29163520, 29164008, 29164020, 29164050, 29164052, 29164144, 29164350, 29164381, 29165130, 29165260, 29165310, 29165680, 29165770, 29166200, 29167183, 29168000, 29168060, 29168064, 29168068, 29168090, 29168470, 29170038, 29172105, 29172810, 29173180, 29173795, 29175269, 29175735, 29176000, 29176970, 29177393, 29182300, 29185000, 29190000, 29190010, 29190014, 29190062, 29190075, 29190300, 29190390, 29190415, 29192154, 29194004, 29194578, 29194590, 29194728, 29197545, 29197551, 29197556, 29197562, 29197900, 29199090, 29201000, 29214400, 29215000, 29220060, 29230000, 29240000, 29260000, 29285000, 29290000, 29300090, 29300130, 29300170, 29300500, 29303300, 29306060, 29306753, 29310390, 29310715, 29345000, 29360000, 29375000, 29380000, 29395000, 29480000, 29490000, 29500000, 29550000, 29645000, 29650000, 29670000, 29680000, 29700010, 29700030, 29700060, 29700070, 29700120, 29700130, 29700200, 29700778, 29700970, 29702591, 29703032, 29703045, 29703100, 29703810, 29705100, 29705200, 29706010, 29706060, 29707615, 29709300, 29750000, 29800000, 29830000, 29845000, 29900010, 29900020, 29900170, 29900190, 29900191, 29900485, 29900495, 29900515, 29900970, 29901290, 29901650, 29902100, 29902520, 29902530, 29904010, 29909035, 29920000, 29927000, 29930180, 29930290, 29930340, 29930650, 29931180, 29931220, 29931225, 29932540, 29933430, 29936450, 29941660, 29950000, 30110929, 30170001, 30180111, 30320765, 30320900, 30330160, 30494270, 31580570, 32040550, 32145792, 32340320, 32425495, 32430370, 32432310, 32600072, 32635002, 32639384, 32900000, 33045170, 33204154, 33400000, 33500900, 33836020, 34525230, 35160198, 35170141, 35260000, 35300112, 35450000, 35570076, 35670972, 35702089, 35703300, 35792363, 36032215, 36033007, 36205319, 36301188, 36400000, 36420000, 36503034, 36572314, 36576402, 36774050, 36855000, 37012015, 37136070, 37136080, 37140000, 37465000, 37470000, 37490000, 37500028, 37500034, 37540000, 37558062, 37600000, 37630000, 37640920, 37930970, 37950000, 38050440, 38066030, 38140000, 38180048, 38301188, 38402037, 38407651, 38411889, 38413296, 38433006, 38444009, 38607260, 38742226, 39200000, 39334400, 39402503, 39404891, 39440504, 39600970, 39625000, 39818970, 39820970, 40140060, 40270060, 40283327, 40300753, 40320350, 40375017, 40391320, 40440360, 40445001, 40490192, 40730580, 41100000, 41150000, 41250160, 41320100, 41500210, 41600500, 41701005, 41720070, 41810001, 41810010, 41810900, 41820020, 41820021, 41820916, 41830150, 41830450, 42700130, 42722020, 42728700, 42739080, 42807009, 42809661, 42812200, 42816150, 42822394, 42825356, 42827550, 42840476, 42841680, 43850000, 44001325, 44001352, 44001525, 44002064, 44002160, 44002168, 44002200, 44002256, 44002392, 44003144, 44006000, 44010630, 44017060, 44020005, 44021440, 44023520, 44024228, 44025500, 44030140, 44032498, 44033150, 44033375, 44033550, 44033710, 44033730, 44035100, 44036240, 44040080, 44043070, 44045000, 44050168, 44050692, 44050794, 44051335, 44051605, 44051900, 44052052, 44052064, 44054128, 44054169, 44056232, 44056539, 44064360, 44066192, 44066596, 44071010, 44073440, 44075465, 44075525, 44077465, 44079002, 44085242, 44085370, 44090005, 44090652, 44091364, 44091612, 44092848, 44140970, 44160000, 44245000, 44254970, 44255000, 44260970, 44448660, 44790000, 45000345, 45000415, 45000545, 45000720, 45000755, 45007150, 45023325, 45025905, 45028135, 45028170, 45029750, 45089340, 45089900, 45106000, 45200970, 45204041, 45260000, 45452000, 45602170, 45611778, 45615970, 45658315, 45810990, 45821736, 45823651, 45983000, 45990096, 46875000, 47813218, 47850000, 48010150, 48115000, 48330000, 48414000, 48474000, 48705000, 48730970, 48793000, 48960000, 48967000, 49027015, 49035612, 49071087, 49090180, 49170000, 49200972, 49290000, 49350000, 49360000, 49390000, 49400970, 49580000, 49650000, 49690000, 49740000, 49800000, 49945000, 49960000, 49980000, 50010928, 50020000, 50070270, 50650100, 50710000, 50720050, 50730100, 50740500, 50770120, 50790185, 51021460, 51250000, 51310000, 51330010, 52011005, 52020020, 52020090, 52040000, 52490000, 53050120, 53350400, 53625375, 54000000, 54080105, 54110000, 54160270, 54250000, 54250080, 54310310, 54325555, 54325718, 54345120, 54410010, 54450020, 54505904, 54518305, 54523090, 54705160, 54720192, 54756262, 54800970, 55000000, 55195284, 55298640, 55425000, 55540971, 55555000, 55592000, 55592970, 55607503, 55608188, 55640320, 55642171, 55900000, 56000000, 56200970, 56203000, 56310780, 56328140, 56460970, 56470000, 56503052, 56950000, 57020470, 57051900, 57071175, 57072304, 57080080, 57100000, 57200991, 57230000, 57230975, 57237000, 57254000, 57302148, 57309610, 57420970, 57440000, 57470970, 57700000, 57900000, 57955000, 58039010, 58073205, 58100000, 58100305, 58233000, 58303135, 58320000, 58323970, 58375000, 58400180, 58580000, 58698971, 58700370, 58705797, 58735000, 58740000, 59010220, 59020000, 59056165, 59068620, 59071300, 59072210, 59074350, 59090050, 59114250, 59122400, 59127000, 59138000, 59138200, 59140200, 59140270, 59140690, 59140765, 59141065, 59141130, 59141170, 59141730, 59143205, 59143270, 59143275, 59144210, 59146750, 59149130, 59149390, 59151250, 59155001, 59157300, 59158172, 59173000, 59179000, 59180000, 59190973, 59196000, 59295132, 59600170, 59643288, 59965000, 60000000, 60010000, 60010270, 60010281, 60010340, 60015052, 60020030, 60025100, 60030100, 60030140, 60030160, 60035110, 60035130, 60040300, 60040350, 60040530, 60050040, 60050081, 60050130, 60055050, 60060090, 60060120, 60110120, 60110300, 60115082, 60115222, 60120002, 60125070, 60125100, 60125150, 60130000, 60130140, 60130240, 60130300, 60130301, 60135100, 60135102, 60140000, 60140061, 60140170, 60150161, 60165078, 60170190, 60170251, 60175195, 60191070, 60310001, 60310018, 60310052, 60310230, 60310520, 60310750, 60310760, 60311750, 60312060, 60320080, 60320104, 60320180, 60320740, 60325000, 60325001, 60325004, 60325210, 60325720, 60330360, 60330460, 60330778, 60331100, 60331320, 60332025, 60332320, 60332360, 60334170, 60335080, 60335110, 60335480, 60336130, 60337180, 60337690, 60340000, 60340005, 60340097, 60340110, 60340197, 60340560, 60341140, 60341510, 60341515, 60341540, 60341545, 60341610, 60341630, 60342150, 60345000, 60345550, 60345660, 60345662, 60346218, 60347230, 60347440, 60347500, 60347610, 60347690, 60348110, 60348200, 60348370, 60349130, 60350550, 60351000, 60351060, 60351100, 60351105, 60351230, 60351250, 60351630, 60351770, 60352130, 60352570, 60352645, 60353020, 60353050, 60353070, 60356150, 60356200, 60356205, 60356590, 60357100, 60360330, 60360332, 60361050, 60410000, 60410220, 60410224, 60410335, 60410424, 60416510, 60420280, 60420530, 60421035, 60421570, 60425140, 60425150, 60425351, 60425400, 60430120, 60430140, 60430560, 60431145, 60440135, 60440160, 60440261, 60440530, 60440588, 60440593, 60440598, 60440605, 60441150, 60441190, 60442140, 60442200, 60442640, 60450360, 60455360, 60455410, 60510010, 60510135, 60510137, 60510138, 60510162, 60510200, 60510205, 60510290, 60510350, 60510390, 60510395, 60510410, 60510430, 60510510, 60520022, 60520101, 60520102, 60520430, 60521045, 60521085, 60521095, 60521105, 60525450, 60525490, 60525540, 60525620, 60525622, 60525632, 60526325, 60530000, 60530060, 60530200, 60530250, 60530390, 60530590, 60530620, 60530810, 60531570, 60532220, 60532590, 60532690, 60533000, 60533530, 60533590, 60533591, 60533610, 60533611, 60533615, 60533620, 60533621, 60533635, 60533640, 60533641, 60533650, 60533651, 60533690, 60533691, 60533692, 60534050, 60534130, 60540120, 60540122, 60540130, 60540134, 60540170, 60540172, 60540230, 60540232, 60540250, 60540252, 60540260, 60540262, 60540280, 60540282, 60540370, 60540374, 60540440, 60540442, 60540446, 60540455, 60540475, 60540492, 60540513, 60540540, 60540542, 60540600, 60540604, 60540732, 60540790, 60541110, 60541112, 60541190, 60541310, 60541490, 60541492, 60541570, 60541575, 60543452, 60543520, 60545100, 60545265, 60545350, 60710000, 60710140, 60710683, 60711475, 60712005, 60712100, 60713590, 60714110, 60714242, 60714252, 60714330, 60714415, 60720001, 60720095, 60720150, 60720600, 60720605, 60721045, 60730000, 60730068, 60730085, 60730155, 60730175, 60730182, 60730220, 60730440, 60730590, 60730640, 60730710, 60731660, 60732380, 60732390, 60740000, 60740350, 60740355, 60741090, 60742200, 60742290, 60743670, 60743680, 60743760, 60743770, 60743790, 60744280, 60745600, 60750080, 60750090, 60750450, 60750550, 60750700, 60750710, 60752100, 60752263, 60752380, 60752477, 60752595, 60760000, 60760540, 60760570, 60762170, 60762200, 60762485, 60762770, 60762775, 60762780, 60762782, 60763430, 60763450, 60763470, 60763580, 60763590, 60763730, 60764240, 60764615, 60765230, 60766230, 60810300, 60810460, 60811690, 60811900, 60822150, 60822915, 60831160, 60831370, 60831400, 60831830, 60832520, 60840000, 60840115, 60840280, 60841032, 60843100, 60850015, 60860000, 60860005, 60860150, 60861030, 60861760, 60862140, 60863210, 60863540, 60864480, 60866290, 60866310, 60866330, 60866490, 60866510, 60866600, 60866680, 60866681, 60867580, 60870010, 60870060, 60870180, 60870200, 60870240, 60870630, 60871170, 60872370, 60872601, 60872690, 60873005, 60873750, 60874060, 60874400, 60875525, 60876685, 61600004, 61600040, 61600180, 61600970, 61603045, 61605180, 61605600, 61605640, 61605680, 61620340, 61634230, 61635180, 61635365, 61645000, 61648100, 61650000, 61650010, 61650110, 61650160, 61652000, 61652520, 61655000, 61656065, 61658030, 61700000, 61751990, 61753000, 61760000, 61800000, 61800970, 61801970, 61809150, 61880000, 61900225, 61900360, 61910000, 61930360, 61939180, 61940005, 61940010, 61948805, 61979000, 62252974, 62598973, 62680000, 62685000, 62690000, 62840000, 62850000, 62870000, 62880001, 62880970, 63010010, 63030250, 63034115, 63143000, 63194000, 63400000, 63430000, 63504268, 63595000, 63870000, 63900153, 64001970, 64002234, 64010000, 64010100, 64028800, 64058700, 64225000, 64230000, 64235970, 64300000, 64540000, 64600026, 64640000, 64675000, 64807520, 64835000, 64890000, 65010340, 65010560, 65015370, 65015440, 65020350, 65020420, 65020450, 65030000, 65031410, 65031455, 65040000, 65050000, 65051210, 65052560, 65052790, 65053190, 65055285, 65061000, 65065470, 65066290, 65067250, 65071380, 65071750, 65074115, 65074199, 65075441, 65076091, 65076150, 65076360, 65085470, 65110000, 65140000, 65145000, 65150000, 65195000, 65200000, 65206000, 65208000, 65213000, 65215000, 65218000, 65223000, 65275000, 65284970, 65300016, 65300112, 65306040, 65345000, 65360000, 65365000, 65370000, 65380970, 65393000, 65398000, 65470970, 65485000, 65607440, 65690000, 65705000, 65706000, 65707000, 65712000, 65718000, 65888000, 65913090, 65930970, 65980970, 66010900, 66085734, 66630000, 66814550, 66913670, 66918600, 67010520, 67030325, 67103330, 67146255, 68015830, 68145970, 68180500, 68220000, 68385000, 68515973, 68543971, 68552412, 68570000, 68600000, 68600972, 68627594, 68644970, 68660970, 68675000, 68701603, 68704030, 68725970, 68741390, 68741650, 68746714, 68746740, 68746770, 68750970, 68780000, 68786970, 68787000, 68901630, 68904670, 68909070, 68928060, 68940000, 68997970, 69005140, 69005430, 69008000, 69010080, 69010090, 69020070, 69020120, 69025315, 69027211, 69030480, 69030670, 69036110, 69037042, 69039540, 69040040, 69041025, 69042010, 69042070, 69045000, 69049120, 69053150, 69058000, 69058810, 69059500, 69063000, 69063010, 69063460, 69065001, 69065150, 69068000, 69068010, 69068020, 69068500, 69070000, 69070250, 69070625, 69073140, 69073175, 69074760, 69075100, 69075180, 69075351, 69076380, 69078000, 69078100, 69079210, 69080430, 69083000);
//        searchList.forEach(cep -> {
//            try {
//                search(cep);
//                Thread.sleep(1000);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });

//        final int initialCep = 10000000;
//        final int qtdCeps = 99999999;
//        final int qtdCeps = 10000;
//        for (int i = 0; i < qtdCeps; i++) {
//            search(initialCep + (i * 1000));
//        }

        final CyclicBarrier gate = new CyclicBarrier(searchList.size() + 1);
        searchList.forEach(cep -> startThread(cep, gate));

        Thread.sleep(500);

        long start = System.currentTimeMillis();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            long end = System.currentTimeMillis();
            log.info("{}ms ellapsed", end - start);
        }));

        gate.await();
    }

    private static void startThread(final Integer numCepInt, final CyclicBarrier gate) {
        var numCep = parseCep(numCepInt);
        new Thread(() -> {
            try {
                gate.await();
                search(numCep);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void search(String numCep) {
        try {
            var client = HttpClient.newBuilder().build();

            var request = HttpRequest.newBuilder(new URI("http://localhost:9595/ceps/%s".formatted(numCep)))
                    .GET()
                    .build();

            log.info("[{}] {} {}", numCep, request.method(), request.uri());
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("[{}] {}: {}", numCep, response.statusCode(), response.body());
        } catch (Exception e) {
            log.error("[{}] {}", numCep, e.getMessage(), e);
        }
    }

}
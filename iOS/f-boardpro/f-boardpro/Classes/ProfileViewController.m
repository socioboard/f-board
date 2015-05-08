//
//  ProfileViewController.m
//  SwitchUserSample
//
//  Created by GBS-ios on 4/25/15.
//
//

#import "ProfileViewController.h"
#import "SUCache.h"
#import "SingletonClass.h"
#import "SingletonClass.h"
#import "TableCustomCell.h"
#import <QuartzCore/QuartzCore.h>
#import "ImageViewCustomCell.h"


@interface ProfileViewController ()

@end

@implementation ProfileViewController


-(void)viewWillAppear:(BOOL)animated{
   self.navigationController.navigationBarHidden=YES;
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(reloadDetails:) name:@"CurrentUserChangedNotification" object:nil];
    
    [self fetchMyDetails];
    
    [self fetchMyProfileDetails];

}
- (void)viewDidLoad {
    [super viewDidLoad];
    
    

    self.photoIdArray = [[NSMutableArray alloc]init];
    self.pictureArray = [[NSMutableArray alloc]init];
    
    self.profileView = [[UIView alloc]init];
    self.profileView.frame = CGRectMake(0, 0, self.view.frame.size.width, 200);
    self.profileView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:self.profileView];
    
    self.navigationController.navigationBar.barTintColor = [UIColor colorWithRed:80.0f/255.0f green:105.0f/255.0f blue:183.0f/255.0f alpha:1.0f];
    [[UINavigationBar appearance] setTintColor:[UIColor blackColor]];
    
    self.coverImageview=[[UIImageView alloc]init];
    self.coverImageview.userInteractionEnabled = YES;
    
    self.coverImageview.frame=CGRectMake(0, 0, self.view.frame.size.width, 250);
//    self.coverImageview.layer.cornerRadius=20;
//    self.coverImageview.layer.masksToBounds = YES;
    [self.profileView addSubview:self.coverImageview];
    
    self.profileImageView=[[FBSDKProfilePictureView alloc]init];
    self.profileImageView.userInteractionEnabled = YES;
    // self.profileImageView.image = [UIImage imageNamed:@"hel.jpeg"];
    self.profileImageView.frame=CGRectMake(30, 110, 80, 80);
    
    [self.profileImageView.layer setBorderColor: [[UIColor blackColor] CGColor]];
    [self.profileImageView.layer setBorderWidth: 2.0];
//    self.profileImageView.layer.cornerRadius=20;
//    self.profileImageView.layer.masksToBounds = YES;
    [self.profileView addSubview:self.profileImageView];
    
    self.nameLabel = [[UILabel alloc]init];
//    self.nameLabel.text = @"Vinayaka";
    self.nameLabel.numberOfLines = 1;
    self.nameLabel.font=[UIFont fontWithName:@"Helvetica-Bold" size:15];
    self.nameLabel.frame = CGRectMake(120, 130, 200, 30);
    [self.profileView addSubview:self.nameLabel];
    
  
    self.navigationController.navigationBar.barTintColor = [UIColor colorWithRed:80.0f/255.0f green:105.0f/255.0f blue:183.0f/255.0f alpha:1.0f];
    [[UINavigationBar appearance] setTintColor:[UIColor blackColor]];
        
    
       // Do any additional setup after loading the view.
}

-(void)reloadDetails:(NSNotificationCenter *)defaultCenter{
    [self fetchMyDetails];
    
    [self fetchMyProfileDetails];


}

-(void)fetchMyProfileDetails{
    NSIndexPath *indexPath=[SingletonClass sharedState].selectedUserIndex;
    FBSDKAccessToken *token = [SUCache itemForSlot:indexPath.row+indexPath.section].token;
    NSString *str=[NSString stringWithFormat:@"/%@?fields=id,name,work,location,hometown,albums,birthday,cover",@"me"];
    if (token) {
        
//        [FBSDKAccessToken setCurrentAccessToken:token];
//        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:@"/me?fields=id,name,work,location,hometown,albums,birthday,cover" parameters:nil];
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:str parameters:nil];
        
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
            NSLog(@"result is %@",result);
            
            self.locationString =[[result objectForKey:@"location"]objectForKey:@"name"];
            self.coverString = [[result objectForKey:@"cover"]objectForKey:@"source"];
            NSLog(@"self.coverstring %@",self.coverString);
            
            [self.coverImageview setImageWithURL:[NSURL URLWithString:self.coverString] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
            
//            [self.profileImageView setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"https://graph.facebook.com/me/picture?type=small"]] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
            
            self.profileImageView.profileID=self.FBid;
            
             for (NSDictionary *dic in [result objectForKey:@"work"] ) {
                  self.workString = [[dic objectForKey:@"employer"]objectForKey:@"name"];
             }

           
            self.homeTownString =[[result objectForKey:@"hometown"]objectForKey:@"name"];
            
            self.nameString =[result objectForKey:@"name"];
            self.nameLabel.text=self.nameString;
            
            self.birthdayString = [result objectForKey:@"birthday"];
            
           
        }];
        [self getUrlOfPhotos];
        [self createFollowTable];
    }

    
    
    
}

-(void)createCollectionView{
    
    UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc] init];
    [flowLayout setItemSize:CGSizeMake(100, 100)];
    [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];
    CGSize s =[UIScreen mainScreen].bounds.size;
    
    if([UIScreen mainScreen].bounds.size.height>500)
    {
        photoCollectionView=[[UICollectionView alloc] initWithFrame:CGRectMake(0, 390, s.width, s.height-390) collectionViewLayout:flowLayout];
    }
    else{
        photoCollectionView=[[UICollectionView alloc] initWithFrame:CGRectMake(0, 360, s.width, s.height-360) collectionViewLayout:flowLayout];
    }
    
    [photoCollectionView setDataSource:self];
    [photoCollectionView setDelegate:self];
    [photoCollectionView registerClass:[ImageViewCustomCell class] forCellWithReuseIdentifier:@"cell"];
    //  [filtercollectioView setBackgroundColor:[UIColor colorWithPatternImage:[UIImage imageNamed:@"background.png"]]];
    [photoCollectionView setBackgroundColor:[UIColor colorWithRed:9/255.0 green:30/255.0 blue:59/255.0 alpha:1]];
    [photoCollectionView setCollectionViewLayout:flowLayout];
    [self.view addSubview:photoCollectionView];

}


#pragma mark collectionview delegates
#pragma mark ----------

-(NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section{
    
    return self.pictureArray.count;
  
}

-(UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{
    
    NSString * identifier=@"cell";
    ImageViewCustomCell * cell=(ImageViewCustomCell *)[collectionView dequeueReusableCellWithReuseIdentifier:identifier forIndexPath:indexPath];
    NSString *str = [self.pictureArray objectAtIndex:indexPath.row];
    cell.photoImgView.hidden=NO;
    [cell.photoImgView setImageWithURL:[NSURL URLWithString:str] placeholderImage:[UIImage imageNamed:@"hel.jpeg"]];
          return cell;
}

-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath{
    

    NSLog(@"index path of row %ld",(long)indexPath.row);
    
}

-(void)createFollowTable
{
    profileTableView=[[UITableView alloc]initWithFrame:CGRectMake(0, 250, self.view.frame.size.width,150) style:UITableViewStylePlain];
    profileTableView.dataSource=self;
    profileTableView.delegate=self;
    profileTableView.backgroundColor=[UIColor whiteColor];
    [self.view addSubview:profileTableView];
}

-(void)viewDidDisappear:(BOOL)animated{
    [[NSNotificationCenter defaultCenter]removeObserver:self name:@"CurrentUserChangedNotification" object:nil];
    
}


-(void)fetchMyDetails{
    self.photoIdArray=[[NSMutableArray alloc] init];
    
    NSIndexPath *indexPath=[SingletonClass sharedState].selectedUserIndex;
   FBSDKAccessToken *token = [SUCache itemForSlot:indexPath.row+indexPath.section].token;
    if (token) {
        NSString *str=[NSString stringWithFormat:@"/%@/albums",@"me"];
        [FBSDKAccessToken setCurrentAccessToken:token];
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:str parameters:nil];
      
        
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
//            NSLog(@"result is %@",result);
            
            NSString *str1 = [result objectForKey:@"id"];

            
            for (NSDictionary *dic in [result objectForKey:@"data"] ) {
                NSString *str1 = [dic objectForKey:@"id"];
                
                if (str1) {
                    [self.photoIdArray addObject:str1];
                }else{
                    [self.photoIdArray addObject:@"nil"];
                }

            }
//            NSLog(@"photo id array is %lu & array is ",(unsigned long)self.photoIdArray.count);
//             NSLog(@"photo id array is %@ ",self.photoIdArray);
            [self getUrlOfPhotos];
        }];
   }
    
}


-(void)getUrlOfPhotos{
   NSIndexPath *indexPath= [SingletonClass sharedState].selectedUserIndex;
    FBSDKAccessToken *token = [SUCache itemForSlot:indexPath.row+indexPath.section].token;
    if (token) {
        
        [FBSDKAccessToken setCurrentAccessToken:token];
        
              //  for (int i=0; i<self.photoIdArray.count; i++) {
            
        if (self.photoIdArray.count>1) {
          
        NSString *str = [self.photoIdArray objectAtIndex:0];
        
        NSString *checkStr = [NSString stringWithFormat:@"%@/photos",str];
        
        FBSDKGraphRequest *request = [[FBSDKGraphRequest alloc] initWithGraphPath:checkStr parameters:nil];
        
        
        [request startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
//            NSLog(@"result is======== %@",result);
            
            for (NSDictionary *dic in [result objectForKey:@"data"] ) {
                NSString *str1 = [dic objectForKey:@"picture"];
                
                if (str1) {
                    [self.pictureArray addObject:str1];
                }else{
                    [self.pictureArray addObject:@"nil"];
                }
                
            }
//            NSLog(@"photo id array is %lu & array is ",(unsigned long)self.pictureArray.count);
//            NSLog(@"photo id array is %@ ",self.pictureArray);
            [self createCollectionView];
        }];
       
       // }
       
    }
    }

}

#pragma mark tableview delegates
#pragma mark ----------

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Display";
    
     UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil)
    {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault
                                       reuseIdentifier:CellIdentifier] ;
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    cell.textLabel.font = [UIFont fontWithName:@"Menlo-BoldItalic" size:12];
    if (indexPath.row ==0) {
        cell.textLabel.text = [NSString stringWithFormat:@"Works at %@",self.workString];
        cell.imageView.image = [UIImage imageNamed:@"briefcase.png"];
    }
    
    if (indexPath.row ==1) {
        cell.textLabel.text =  [NSString stringWithFormat:@"Lives at %@",self.locationString];
        cell.imageView.image = [UIImage imageNamed:@"place.png"];
   }
    
    if (indexPath.row ==2) {
        cell.textLabel.text =   [NSString stringWithFormat:@"Hometown  %@",self.homeTownString];
        cell.imageView.image = [UIImage imageNamed:@"home.png"];
  }
    
    if (indexPath.row ==3) {
        cell.textLabel.text = [NSString stringWithFormat:@"Birthday  %@",self.birthdayString];
        cell.imageView.image = [UIImage imageNamed:@"briefcase.png"];

    }
    return cell;
    
}


-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath{
    
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
}

-(NSInteger) numberOfSectionsInTableView:(UITableView *)tableView
{
    
    return 1;
}

-(NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 4;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 35;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
